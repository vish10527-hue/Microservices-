package in.ameya.schedulers.schedulersUtil.ameyaSwiftMsgsParser9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.ameya.schedulers.schedulersUtil.util.ReturnValue;
import in.ameya.schedulers.schedulersUtil.util.SchedulerUtil;
import in.ameya.schedulers.service.mtSeriesService.AmeyaSwiftMsgsService;
import in.ameya.schedulers.vo.mtSeries.MsgType9;
import in.ameya.schedulers.vo.mtSeries.Type61Data;
import in.ameya.schedulers.vo.mtSeries.Type910;
import in.ameya.schedulers.vo.mtSeries.Type940;
import in.ameya.schedulers.vo.mtSeries.Type950;
import in.ameya.schedulers.vo.mtSeries.Type999;
import in.ameya.util.LoggerUtil;


@Component
public class SwiftParser9 
{
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	SchedulerUtil schedulerUtil;
	
	@Autowired
	AmeyaSwiftMsgsService ameyaSwiftMsgsService;
	
	
	public int processInOutFiles(int intRecordID, MsgType9 msgType, String strInputDirectoryWithFileName) throws IOException
	{
	
		int errorCode = -1;
		
		try{
			switch(msgType)
			{
				case MT910:
					if(schedulerUtil.GetSwiftFileType(strInputDirectoryWithFileName).equalsIgnoreCase("910"))
					{
						Object objOutput =  UploadMT910(strInputDirectoryWithFileName, fnGetActivityCode(msgType));						
						return GetInsertStatement(intRecordID, msgType, objOutput) ;
					}
					else
					{
						return -1;
					}
				case MT940:
					if(schedulerUtil.GetSwiftFileType(strInputDirectoryWithFileName).equalsIgnoreCase("940"))
					{
						Object objOutput =  UploadMT940(strInputDirectoryWithFileName, fnGetActivityCode(msgType));						
						return GetInsertStatement(intRecordID, msgType, objOutput) ;
					}
					else if(schedulerUtil.GetSwiftFileType(strInputDirectoryWithFileName).equalsIgnoreCase("950"))
					{
						Object objOutput =  UploadMT950(strInputDirectoryWithFileName, fnGetActivityCode(msgType));						
						return GetInsertStatement(intRecordID, msgType, objOutput) ;
					}
					else
					{
						return -1;
					}					
				case MT950:
					if(schedulerUtil.GetSwiftFileType(strInputDirectoryWithFileName).equalsIgnoreCase("940"))
					{
						Object objOutput =  UploadMT940(strInputDirectoryWithFileName, fnGetActivityCode(msgType));						
						return GetInsertStatement(intRecordID, msgType, objOutput) ;
					}
					else if(schedulerUtil.GetSwiftFileType(strInputDirectoryWithFileName).equalsIgnoreCase("950"))
					{
						Object objOutput =  UploadMT950(strInputDirectoryWithFileName, fnGetActivityCode(msgType));						
						return GetInsertStatement(intRecordID, msgType, objOutput) ;
					}
					else
					{
						return -1;
					}
				case MT999:
					if(schedulerUtil.GetSwiftFileType(strInputDirectoryWithFileName).equalsIgnoreCase("999"))
					{
						Object objOutput =  UploadMT999(strInputDirectoryWithFileName, fnGetActivityCode(msgType));						
						return GetInsertStatement(intRecordID, msgType, objOutput) ;
					}
					else
					{
						return -1;
					}
			default:
				break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SwiftParser9-fnParseFile", "");
		}
		return errorCode;
	}
		
	private Object UploadMT910(String FilePath, Integer ActivityCode) throws IOException
	{
		ArrayList<Hashtable<String, Integer>> lstTags = new ArrayList<Hashtable<String,Integer>>();
		ArrayList<ArrayList<String>> lstRecords = schedulerUtil.fnReadFileIntoList(FilePath, lstTags);
		ArrayList<String> completeMsgList = schedulerUtil.getMessageString(FilePath);
		Integer intPosition;
		Integer Current = 0;
		String strData;
		Type910 objType910;
		ArrayList<Type910> lstType910 = new ArrayList<Type910>();
		ReturnValue objRetVal = new ReturnValue();
		
		try{
			for (ArrayList<String> lstRecord : lstRecords) 
			{
				lstType910.add(new Type910());
				objType910 = lstType910.get(Current);
				objType910.setActivityCode(String.valueOf(ActivityCode));
				
				String completeMsgStr = completeMsgList.get(Current);
				objType910.setCompleteMsgStr(completeMsgStr);
				
				//ackNackedFlag
				if(lstRecord.size() > 0)
					objType910.setAckNackedFlag(schedulerUtil.GetAckNackedFlag(lstRecord.get(0)));
				
				String inwardOutwardType = schedulerUtil.GetInwardOutwardType(lstRecord.get(0));
				String senderCode = "";
				String recCode = "";
				
				if(("2").equalsIgnoreCase(inwardOutwardType))
				{
					 senderCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
					 recCode = schedulerUtil.SenderCode(lstRecord.get(0)).substring(0,8);
				}
				else
				{
					 senderCode = schedulerUtil.SenderCode(lstRecord.get(0));
					 recCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
				}
				
				//inwardOutwardType
				if(lstRecord.size() > 0)
					objType910.setInwardOutwardType(inwardOutwardType);
				
				//Sender Code
				if(lstRecord.size() > 0)
					objType910.setSenderCode(senderCode);
				
				//Receiver BIC
				if(lstRecord.size() > 0)
					objType910.setReceiverBIC(recCode);
				
				//TRF No
				if(lstTags.get(Current).containsKey(":20:"))
					objType910.setTRFNo(schedulerUtil.TRFNo(lstRecord.get(lstTags.get(Current).get(":20:"))));
				
				//RelRef
				strData = ":21:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType910.setRelRef(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				//AccountID
				strData = ":25:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType910.setAccountID(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				//valDate, CurrCode, Amount
				if(lstTags.get(Current).containsKey(":32A:"))
				{
					objType910.setValDate(schedulerUtil.ValDate(lstRecord.get(lstTags.get(Current).get(":32A:")), ":32A:"));
					objType910.setCurrCode(schedulerUtil.CurrCode(lstRecord.get(lstTags.get(Current).get(":32A:")), ":32A:"));
					objType910.setAmount(schedulerUtil.Amount(lstRecord.get(lstTags.get(Current).get(":32A:")), ":32A:"));
				}
				
				//OrdCustInfo And Address
				strData = ":50";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType910.setOrdCustInf(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData) + 
							schedulerUtil.fnGetCustomData(lstRecord, intPosition, 3).strRetVal);
				
				//OrdInst
				strData = ":52";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType910.setOrdInst(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData) + 
							schedulerUtil.fnGetCustomData(lstRecord, intPosition, 5).strRetVal);
				
				//Intermed
				strData = ":56";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType910.setIntermed(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData) + 
							schedulerUtil.fnGetCustomData(lstRecord, intPosition, 5).strRetVal);
				
				//AccountDetails
				strData = ":57";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType910.setAccountDetails(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData) + 
							schedulerUtil.fnGetCustomData(lstRecord, intPosition, 5).strRetVal);
				
				//SenderInfo
				strData = ":72:"; //Max 6 lines
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType910.setSenderInfo(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData) + 
							schedulerUtil.fnGetCustomData(lstRecord, intPosition, 5).strRetVal);
				Current++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SwiftParser9-UploadMT910", "");
		}
		return lstType910;
	}
	
	private Object UploadMT940(String FilePath, Integer ActivityCode) throws IOException
	{
		ArrayList<Hashtable<String, Integer>> lstTags = new ArrayList<Hashtable<String,Integer>>();
		ArrayList<ArrayList<String>> lstRecords = schedulerUtil.fnReadFileIntoList(FilePath, lstTags);
		ArrayList<String> completeMsgList = schedulerUtil.getMessageString(FilePath);
		Integer intPosition;
		Integer Current = 0;
		String strData;
		Type940 objType940;
		ArrayList<Type940> lstType940 = new ArrayList<Type940>();
		ReturnValue objRetVal = new ReturnValue();
		
		try{
			for (ArrayList<String> lstRecord : lstRecords) 
			{
				lstType940.add(new Type940());
				objType940 = lstType940.get(Current);
				objType940.setMessageNo(Current + 1);
				objType940.setActivityCode(String.valueOf(ActivityCode));
				
				String completeMsgStr = completeMsgList.get(Current);
				objType940.setCompleteMsgStr(completeMsgStr);
				
				//ackNackedFlag
				if(lstRecord.size() > 0)
					objType940.setAckNackedFlag(schedulerUtil.GetAckNackedFlag(lstRecord.get(0)));
				
				String inwardOutwardType = schedulerUtil.GetInwardOutwardType(lstRecord.get(0));
				String senderCode = "";
				String recCode = "";
				
				if(("2").equalsIgnoreCase(inwardOutwardType))
				{
					 senderCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
					 recCode = schedulerUtil.SenderCode(lstRecord.get(0)).substring(0,8);
				}
				else
				{
					 senderCode = schedulerUtil.SenderCode(lstRecord.get(0));
					 recCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
				}
				
				//inwardOutwardType
				if(lstRecord.size() > 0)
					objType940.setInwardOutwardType(inwardOutwardType);

				//Sender Code
				if(lstRecord.size() > 0)
					objType940.setSenderCode(senderCode);
				
				//Receiver BIC
				if(lstRecord.size() > 0)
					objType940.setReceiverBIC(recCode);
				
				//TRF No
				if(lstTags.get(Current).containsKey(":20:"))
					objType940.setTRFNo(schedulerUtil.TRFNo(lstRecord.get(lstTags.get(Current).get(":20:"))));
				
				
				//REL REF
				strData = ":21:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType940.setRelRef(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				//AccountID
				strData = ":25:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType940.setAccountID(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				
				strData = ":25P:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType940.setAccountID(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				//StatOrSeq
				strData = ":28C:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType940.setStatOrSeq(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				//CurrCode, Amount
				strData = ":60";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
				{
					int intTempPos = lstRecord.get(intPosition).indexOf(":60") + 5;
					
					//Fetch Transaction Type (C,D,RD,RC (opening balance))
					objType940.setTranType(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 1));
					
					intTempPos += 1;
					//Set Value Date (opening balance)
					objType940.setValueDate(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 6));
					
					intTempPos += 6;
					//Set currency code (opening balance)
					objType940.setCurrCode(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 3));
					
					intTempPos += 3;
					//set amount (opening balance)
					String strAmount = lstRecord.get(intPosition).substring(intTempPos);
					strAmount = strAmount.replace(",", ".").replace(" ", "");
					if(strAmount.indexOf(".") + 1 == strAmount.length())
						strAmount += "00";
					else if(!schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1)))
						strAmount = strAmount.substring(strAmount.indexOf(".")) + ".00";
					if(lstRecord.get(intPosition).indexOf(":60M") > -1)
					{
						objType940.setMiddleAmount(strAmount);
						objType940.setOpType(0);
					}
					else
					{
						objType940.setAmount(strAmount);
						objType940.setOpType(1);
					}
				}
				
				for(intPosition = 0; intPosition < lstRecord.size(); intPosition++)
					if(lstRecord.get(intPosition).indexOf(":61:") > -1) 
					{
						break;
					}
				
				if(intPosition < lstRecord.size() - 1)
				{
					int intCount = -1;
					Type61Data objType61Data;
					for(int i = intPosition; i < lstRecord.size(); i++)
					{
						if(lstRecord.get(i).indexOf(":61:") > -1)
						{
							objType940.getType61DataList().add(new Type61Data());
							intCount++;
							objType61Data = objType940.getType61DataList().get(intCount); 
							objType61Data.setStatLine(lstRecord.get(i).replace(":61:", ""));
							objType61Data.setValDate(objType61Data.getStatLine().substring(0,6));
							i++;
							
							if(i < lstRecord.size())
							{
								strData = lstRecord.get(i);
								if(objType61Data.getStatLine().substring(6, 7).equalsIgnoreCase("R"))
								{
									objType61Data.setTranType(objType61Data.getStatLine().substring(6, 8));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(8, 9)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(8));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(9));
								}
								else if(objType61Data.getStatLine().substring(6, 7).equalsIgnoreCase("D") ||
										objType61Data.getStatLine().substring(6, 7).equalsIgnoreCase("C"))
								{
									objType61Data.setTranType(objType61Data.getStatLine().substring(6, 7));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(7, 8)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(7));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(8));
								}
								else if(objType61Data.getStatLine().substring(10, 11).equalsIgnoreCase("R"))
								{
									objType61Data.setEntryDate(objType61Data.getStatLine().substring(0, 2) + 
											objType61Data.getStatLine().substring(6, 10));
									objType61Data.setTranType(objType61Data.getStatLine().substring(10, 12));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(12, 13)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(12));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(13));
								}
								else if(objType61Data.getStatLine().substring(10, 11).equalsIgnoreCase("D") ||
										objType61Data.getStatLine().substring(10, 11).equalsIgnoreCase("C"))
								{
									objType61Data.setEntryDate(objType61Data.getStatLine().substring(0, 2) + 
											objType61Data.getStatLine().substring(6, 10));
									objType61Data.setTranType(objType61Data.getStatLine().substring(10, 11));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(11, 12)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(11));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(12));
								}
								
								String strAmount = objType61Data.getDealAmount();
								strAmount = strAmount.substring(0, strAmount.indexOf(",") + 3);
								strAmount = strAmount.replace(",", ".").replace(" ", "");
								if(strAmount.indexOf("." ) + 1 == strAmount.length())
									strAmount += "00";
								else
								{
									if(!schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1)))
									{
										if(schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1, strAmount.indexOf(".") + 2)))
											strAmount = strAmount.substring(0, strAmount.indexOf(".") + 2) + "0";
										else
											strAmount = strAmount.substring(0, strAmount.indexOf(".") + 1) + "00";
									}
								}
								objType61Data.setDealAmount(strAmount);
								
								String strStatLine = objType61Data.getStatLine();
								String strTranRefNo = "";
								if(strStatLine.indexOf("//") > -1)
								{
									objType61Data.setMTRefno(strStatLine.substring(strStatLine.indexOf("//") + 2));
									if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 2)))
									{
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 1);
										strTranRefNo = strTranRefNo.substring(0, strTranRefNo.indexOf("//"));
									}
									else if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 3)))
									{
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 2);
										strTranRefNo = strTranRefNo.substring(0, strTranRefNo.indexOf("//"));
									}
									else
									{
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 3);
										strTranRefNo = strTranRefNo.substring(0, strTranRefNo.indexOf("//"));
									}
								}
								else
								{
									objType61Data.setMTRefno("");
									if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 2)))
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 1);
									else if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 3)))
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 2);
									else
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 3);
								}
								
								/*if(strTranRefNo.length() > 16)
								{
									objType61Data.setTransTypeIdentificationCode(strTranRefNo.substring(0, 4));
								}*/
								
								String line61 = objType61Data.getStatLine();
								if(line61 != null)
								{
									int index = line61.indexOf(",");
									if(index > 0 && line61.length() > index +7)
									{
										String transTypeIndfyCode = "";
										String firstTwoChar = line61.substring(index+1, index+3);
										Pattern pattern = Pattern.compile("^[0-9]");
										Matcher matcher = pattern.matcher(firstTwoChar);
										if(matcher.find())
										{
											transTypeIndfyCode = line61.substring(index+4, index+7);
										}
										else
										{
											transTypeIndfyCode = line61.substring(index+2, index+5);
										}
										objType61Data.setTransTypeIdentificationCode(transTypeIndfyCode);
									}
								}
								
								if(strTranRefNo.length() >= 4)
									strTranRefNo = strTranRefNo.substring(4);
								else
									strTranRefNo = "";
								objType61Data.setTranRefNo(strTranRefNo);
								
								
								String strDataTemp = ":86:";
								String strTempBankDetail = "";
								
								strTempBankDetail = lstRecord.get(i) + " ";
								
								if(strTempBankDetail.indexOf(":") > -1)
									strTempBankDetail = "";
								
								boolean blnFound = false;
								
								for(int j = i; j < lstRecord.size(); j++)
								{
									if(lstRecord.get(j).indexOf(strDataTemp) > -1)
									{
										i = j;
										blnFound = true;
										break;
									}
									else if(lstRecord.get(j).indexOf(":") == 0)
										break;
								}
								
								if(blnFound)
								{
									objType61Data.setField86Val(schedulerUtil.CustomTags(lstRecord.get(i), strDataTemp));
									if(i < lstRecord.size())
									{
										for(int k = i + 1; k < lstRecord.size(); k++)
										{
											if(lstRecord.get(k).indexOf(":61:") == -1 &&
												lstRecord.get(k).indexOf(":62M:") == -1 &&
												lstRecord.get(k).indexOf(":62F:") == -1)
												objType61Data.setField86Val(objType61Data.getField86Val() +
														lstRecord.get(k));
											else
											{
												i = k - 1;
												break;
											}
										}
									}
								}
								
								if(strTempBankDetail.trim() != "")
									strTempBankDetail = strTempBankDetail + " #!$~@##!$~@# ";
								
								objType61Data.setField86Val((strTempBankDetail + objType61Data.getField86Val()).trim());
								
								if(strData.indexOf(":62F:") > -1 || strData.indexOf(":62M:") > -1)
								{
									//set closing balance details
									int intTempPos = strData.indexOf(":62") + 5;
									
									//Fetch Transaction Type (C,D,RD,RC (opening balance))
									objType940.setClosingTranType(strData.substring(intTempPos, intTempPos + 1));
									
									intTempPos += 1;
									//Set Value Date (opening balance)
									objType940.setClosingValueDate(strData.substring(intTempPos, intTempPos + 6));
									
									intTempPos += 6;
									//Set currency code (opening balance)
									objType940.setClosingCurrCode(strData.substring(intTempPos, intTempPos + 3));
									
									intTempPos += 3;
									//set amount (opening balance)
									String strClosingAmount = strData.substring(intTempPos);
									strClosingAmount = strClosingAmount.replace(",", ".").replace(" ", "");
									if(strClosingAmount.indexOf(".") + 1 == strClosingAmount.length())
										strClosingAmount += "00";
									else if(!schedulerUtil.IsNumeric(strClosingAmount.substring(strClosingAmount.indexOf(".") + 1)))
										strClosingAmount = strClosingAmount.substring(strClosingAmount.indexOf(".")) + ".00";
									//if(lstRecord.get(intPosition).indexOf(":62M") > -1)
									if(strData.indexOf(":62M") > -1)
									{
										objType940.setClosingMiddleAmount(strClosingAmount);
										objType940.setClType(0);
									}
									else
									{
										objType940.setClosingAmount(strClosingAmount);
										objType940.setClType(1);
									}
									//end set closing balance details
									
									String strTempBankDetails = "";
									if(strStatLine.length() >= 16 &&
										objType61Data.getMTRefno().trim().equalsIgnoreCase(""))
										strTempBankDetails = strStatLine.substring(strStatLine.length() - 16);
									objType61Data.setBankDetails(strTempBankDetails.trim());
									break;
								}
								else
								{
									if(strData.indexOf(":61:") > -1)
									{
										strData = "";
										i -= 1;
									}
									
									String strTempBankDetails = "";
									if(strStatLine.length() >= 16 &&
										objType61Data.getMTRefno().trim().equalsIgnoreCase(""))
										strTempBankDetails = strStatLine.substring(strStatLine.length() - 16);
									if(strData.indexOf(":86:") > -1) strData = "";
									objType61Data.setBankDetails((strTempBankDetails.trim() + " " + strData).trim());
								}
							}
						}
						else
						{
							strData = lstRecord.get(i);
							if(strData.indexOf(":62") == 0)
							{
								//set closing balance details
								int intTempPos = strData.indexOf(":62") + 5;
								
								//Fetch Transaction Type (C,D,RD,RC (opening balance))
								objType940.setClosingTranType(strData.substring(intTempPos, intTempPos + 1));
								
								intTempPos += 1;
								//Set Value Date (opening balance)
								objType940.setClosingValueDate(strData.substring(intTempPos, intTempPos + 6));
								
								intTempPos += 6;
								//Set currency code (opening balance)
								objType940.setClosingCurrCode(strData.substring(intTempPos, intTempPos + 3));
								
								intTempPos += 3;
								//set amount (opening balance)
								String strClosingAmount = strData.substring(intTempPos);
								strClosingAmount = strClosingAmount.replace(",", ".").replace(" ", "");
								if(strClosingAmount.indexOf(".") + 1 == strClosingAmount.length())
									strClosingAmount += "00";
								else if(!schedulerUtil.IsNumeric(strClosingAmount.substring(strClosingAmount.indexOf(".") + 1)))
									strClosingAmount = strClosingAmount.substring(strClosingAmount.indexOf(".")) + ".00";
								//if(lstRecord.get(intPosition).indexOf(":62M") > -1)
								if(strData.indexOf(":62M") > -1)
								{
									objType940.setClosingMiddleAmount(strClosingAmount);
									objType940.setClType(0);
								}
								else
								{
									objType940.setClosingAmount(strClosingAmount);
									objType940.setClType(1);
								}
								//end set closing balance details
							}
							
						}
					}
				}
				else
				{
					//CurrCode, Amount
					strData = ":62";
					objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
					intPosition = objRetVal.intRetVal;
					strData = objRetVal.strRetVal;
					if(intPosition > -1)
					{
						strData = lstRecord.get(intPosition);
						if(strData.indexOf(":62") == 0)
						{
							//set closing balance details
							int intTempPos = strData.indexOf(":62") + 5;
							
							//Fetch Transaction Type (C,D,RD,RC (opening balance))
							objType940.setClosingTranType(strData.substring(intTempPos, intTempPos + 1));
							
							intTempPos += 1;
							//Set Value Date (opening balance)
							objType940.setClosingValueDate(strData.substring(intTempPos, intTempPos + 6));
							
							intTempPos += 6;
							//Set currency code (opening balance)
							objType940.setClosingCurrCode(strData.substring(intTempPos, intTempPos + 3));
							
							intTempPos += 3;
							//set amount (opening balance)
							String strClosingAmount = strData.substring(intTempPos);
							strClosingAmount = strClosingAmount.replace(",", ".").replace(" ", "");
							if(strClosingAmount.indexOf(".") + 1 == strClosingAmount.length())
								strClosingAmount += "00";
							else if(!schedulerUtil.IsNumeric(strClosingAmount.substring(strClosingAmount.indexOf(".") + 1)))
								strClosingAmount = strClosingAmount.substring(strClosingAmount.indexOf(".")) + ".00";
							//if(lstRecord.get(intPosition).indexOf(":62M") > -1)
							if(strData.indexOf(":62M") > -1)
							{
								objType940.setClosingMiddleAmount(strClosingAmount);
								objType940.setClType(0);
							}
							else
							{
								objType940.setClosingAmount(strClosingAmount);
								objType940.setClType(1);
							}
							//end set closing balance details
						}
					}
				}
				
				//----------------------Closing Available Balance (Available Funds)-------
				strData = ":64";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
				{
					//int intTempPos = lstRecord.get(intPosition).indexOf(":64") + 4;
					int intTempPos = strData.indexOf(":64") + 4;
					
					//Fetch Transaction Type (C,D (Available Funds))
					objType940.setClosingAvalBalTranType(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 1));
					
					intTempPos += 1;
					//Set Value Date (Available Funds)
					objType940.setClosingAvalBalDate(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 6));
					
					intTempPos += 6;
					//Set currency code (Available Funds)
					objType940.setClosingAvalBalCurrCode(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 3));
					
					intTempPos += 3;
					//set amount (Available Funds)
					String strAmount = lstRecord.get(intPosition).substring(intTempPos);
					strAmount = strAmount.replace(",", ".").replace(" ", "");
					if(strAmount.indexOf(".") + 1 == strAmount.length())
						strAmount += "00";
					else if(!schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1)))
						strAmount = strAmount.substring(strAmount.indexOf(".")) + ".00";
					if(lstRecord.get(intPosition).indexOf(":64") > -1)
					{
						objType940.setClosingAvalBalAmount(strAmount);
		    		}
				}
				
				//----------------------Forward Available Balance-------
				strData = ":65";
				objRetVal = schedulerUtil.fnFindByTagsDescMultiple(lstTags.get(Current), strData);
				if(objRetVal.intRetValArr != null)
				{
					if(objRetVal.intRetValArr.length > 0 )
					{
						String strForwardAvailableBalanceTemp = "";
						for(int i = 0; i < objRetVal.intRetValArr.length; i++)
						{
							intPosition = objRetVal.intRetValArr[i];
							strData = objRetVal.strRetValArr[i];
							if(intPosition > -1)
							{
								if(("").equalsIgnoreCase(strForwardAvailableBalanceTemp))
								{
									strForwardAvailableBalanceTemp = schedulerUtil.CustomTags(lstRecord.get(intPosition), strData);
								}
								else
								{
									strForwardAvailableBalanceTemp = strForwardAvailableBalanceTemp + "|" + schedulerUtil.CustomTags(lstRecord.get(intPosition), strData);
								}
							}
						}
						objType940.setForwardAvailableBalance(strForwardAvailableBalanceTemp.replaceAll(",", "."));
					}
				}
				
				Current++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SwiftParser9-UploadMT940", "");
		}
		return lstType940;
	}
	
	private Object UploadMT950(String FilePath, Integer ActivityCode) throws IOException
	{
		ArrayList<Hashtable<String, Integer>> lstTags = new ArrayList<Hashtable<String,Integer>>();
		ArrayList<ArrayList<String>> lstRecords = schedulerUtil.fnReadFileIntoList(FilePath, lstTags);
		ArrayList<String> completeMsgList = schedulerUtil.getMessageString(FilePath);
		Integer intPosition;
		Integer Current = 0;
		String strData;
		Type950 objType950;
		ArrayList<Type950> lstType950 = new ArrayList<Type950>();
		ReturnValue objRetVal = new ReturnValue();
		
		try{
			for (ArrayList<String> lstRecord : lstRecords) 
			{
				lstType950.add(new Type950());
				objType950 = lstType950.get(Current);
				objType950.setMessageNo(Current + 1);
				objType950.setActivityCode(String.valueOf(ActivityCode));
				
				String completeMsgStr = completeMsgList.get(Current);
				objType950.setCompleteMsgStr(completeMsgStr);
				
				//ackNackedFlag
				if(lstRecord.size() > 0)
					objType950.setAckNackedFlag(schedulerUtil.GetAckNackedFlag(lstRecord.get(0)));
				
				String inwardOutwardType = schedulerUtil.GetInwardOutwardType(lstRecord.get(0));
				String senderCode = "";
				String recCode = "";
				
				if(("2").equalsIgnoreCase(inwardOutwardType))
				{
					 senderCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
					 recCode = schedulerUtil.SenderCode(lstRecord.get(0)).substring(0,8);
				}
				else
				{
					 senderCode = schedulerUtil.SenderCode(lstRecord.get(0));
					 recCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
				}
				
				//inwardOutwardType
				if(lstRecord.size() > 0)
					objType950.setInwardOutwardType(inwardOutwardType);

				//Sender Code
				if(lstRecord.size() > 0)
					objType950.setSenderCode(senderCode);
				
				//Receiver BIC
				if(lstRecord.size() > 0)
					objType950.setReceiverBIC(recCode);
				
				//TRF No
				if(lstTags.get(Current).containsKey(":20:"))
					objType950.setTRFNo(schedulerUtil.TRFNo(lstRecord.get(lstTags.get(Current).get(":20:"))));
				
				//AccountID
				strData = ":25:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType950.setAccountID(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				strData = ":25P:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType950.setAccountID(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				//StatOrSeq
				strData = ":28C:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
					objType950.setStatOrSeq(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				
				//CurrCode, Amount
				strData = ":60";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
				{
					int intTempPos = lstRecord.get(intPosition).indexOf(":60") + 5;
					
					//Fetch Transaction Type (C,D,RD,RC (opening balance))
					objType950.setTranType(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 1));
					
					intTempPos += 1;
					//Set Value Date (opening balance)
					objType950.setValueDate(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 6));
					
					intTempPos += 6;
					//Set currency code (opening balance)
					objType950.setCurrCode(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 3));
					
					intTempPos += 3;
					//set amount (opening balance)
					String strAmount = lstRecord.get(intPosition).substring(intTempPos);
					strAmount = strAmount.replace(",", ".").replace(" ", "");
					if(strAmount.indexOf(".") + 1 == strAmount.length())
						strAmount += "00";
					else if(!schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1)))
						strAmount = strAmount.substring(strAmount.indexOf(".")) + ".00";
					if(lstRecord.get(intPosition).indexOf(":60M") > -1)
					{
						objType950.setMiddleAmount(strAmount);
						objType950.setOpType(0);
					}
					else
					{
						objType950.setAmount(strAmount);
						objType950.setOpType(1);
					}
				}
				
				for(intPosition = 0; intPosition < lstRecord.size(); intPosition++)
					if(lstRecord.get(intPosition).indexOf(":61:") > -1) 
					{
						break;
					}
				
				if(intPosition < lstRecord.size() - 1)
				{
					int intCount = -1;
					Type61Data objType61Data;
					for(int i = intPosition; i < lstRecord.size(); i++)
					{
						if(lstRecord.get(i).indexOf(":61:") > -1)
						{
							objType950.getType61DataList().add(new Type61Data());
							intCount++;
							objType61Data = objType950.getType61DataList().get(intCount);
							objType61Data.setStatLine(lstRecord.get(i).replace(":61:", ""));
							objType61Data.setValDate(objType61Data.getStatLine().substring(0,6));
							i++;
							
							if(i < lstRecord.size())
							{
								strData = lstRecord.get(i);
								if(objType61Data.getStatLine().substring(6, 7).equalsIgnoreCase("R"))
								{
									objType61Data.setTranType(objType61Data.getStatLine().substring(6, 8));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(8, 9)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(8));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(9));
								}
								else if(objType61Data.getStatLine().substring(6, 7).equalsIgnoreCase("D") ||
										objType61Data.getStatLine().substring(6, 7).equalsIgnoreCase("C"))
								{
									objType61Data.setTranType(objType61Data.getStatLine().substring(6, 7));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(7, 8)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(7));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(8));
								}
								else if(objType61Data.getStatLine().substring(10, 11).equalsIgnoreCase("R"))
								{
									objType61Data.setEntryDate(objType61Data.getStatLine().substring(0, 2) + 
											objType61Data.getStatLine().substring(6, 10));
									objType61Data.setTranType(objType61Data.getStatLine().substring(10, 12));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(12, 13)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(12));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(13));
								}
								else if(objType61Data.getStatLine().substring(10, 11).equalsIgnoreCase("D") ||
										objType61Data.getStatLine().substring(10, 11).equalsIgnoreCase("C"))
								{
									objType61Data.setEntryDate(objType61Data.getStatLine().substring(0, 2) + 
											objType61Data.getStatLine().substring(6, 10));
									objType61Data.setTranType(objType61Data.getStatLine().substring(10, 11));
									if(schedulerUtil.IsNumeric(objType61Data.getStatLine().substring(11, 12)))
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(11));
									else
										objType61Data.setDealAmount(objType61Data.getStatLine().substring(12));
								}
								
								String strAmount = objType61Data.getDealAmount();
								strAmount = strAmount.substring(0, strAmount.indexOf(",") + 3);
								strAmount = strAmount.replace(",", ".").replace(" ", "");
								if(strAmount.indexOf("." ) + 1 == strAmount.length())
									strAmount += "00";
								else
								{
									if(!schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1)))
									{
										if(schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1, strAmount.indexOf(".") + 2)))
											strAmount = strAmount.substring(0, strAmount.indexOf(".") + 2) + "0";
										else
											strAmount = strAmount.substring(0, strAmount.indexOf(".") + 1) + "00";
									}
								}
								objType61Data.setDealAmount(strAmount);
								
								String strStatLine = objType61Data.getStatLine();
								String strTranRefNo = "";
								if(strStatLine.indexOf("//") > -1)
								{
									objType61Data.setMTRefno(strStatLine.substring(strStatLine.indexOf("//") + 2));
									if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 2)))
									{
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 1);
										strTranRefNo = strTranRefNo.substring(0, strTranRefNo.indexOf("//"));
									}
									else if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 3)))
									{
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 2);
										strTranRefNo = strTranRefNo.substring(0, strTranRefNo.indexOf("//"));
									}
									else
									{
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 3);
										strTranRefNo = strTranRefNo.substring(0, strTranRefNo.indexOf("//"));
									}
								}
								else
								{
									objType61Data.setMTRefno("");
									if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 2)))
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 1);
									else if(!schedulerUtil.IsNumeric(strStatLine.substring(strStatLine.indexOf(",") + 1, 
											strStatLine.indexOf(",") + 3)))
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 2);
									else
										strTranRefNo = strStatLine.substring(strStatLine.indexOf(",") + 3);
									
								}
								
								/*if(strTranRefNo.length() > 16)
								{
									objType61Data.setTransTypeIdentificationCode(strTranRefNo.substring(0, 4));
								}*/
								
								String line61 = objType61Data.getStatLine();
								if(line61 != null)
								{
									int index = line61.indexOf(",");
									if(index > 0 && line61.length() > index +7)
									{
										String transTypeIndfyCode = "";
										String firstTwoChar = line61.substring(index+1, index+3);
										Pattern pattern = Pattern.compile("^[0-9]");
										Matcher matcher = pattern.matcher(firstTwoChar);
										if(matcher.find())
										{
											transTypeIndfyCode = line61.substring(index+4, index+7);
										}
										else
										{
											transTypeIndfyCode = line61.substring(index+2, index+5);
										}
										objType61Data.setTransTypeIdentificationCode(transTypeIndfyCode);
									}
								}
								
								if(strTranRefNo.length() >= 4)
									strTranRefNo = strTranRefNo.substring(4);
								else
									strTranRefNo = "";
								objType61Data.setTranRefNo(strTranRefNo);
								
								if(strData.indexOf(":62F:") > -1 || strData.indexOf(":62M:") > -1)
								{
									//set closing balance details
									int intTempPos = strData.indexOf(":62") + 5;
									
									//Fetch Transaction Type (C,D,RD,RC (opening balance))
									objType950.setClosingTranType(strData.substring(intTempPos, intTempPos + 1));
									
									intTempPos += 1;
									//Set Value Date (opening balance)
									objType950.setClosingValueDate(strData.substring(intTempPos, intTempPos + 6));
									
									intTempPos += 6;
									//Set currency code (opening balance)
									objType950.setClosingCurrCode(strData.substring(intTempPos, intTempPos + 3));
									
									intTempPos += 3;
									//set amount (opening balance)
									String strClosingAmount = strData.substring(intTempPos);
									strClosingAmount = strClosingAmount.replace(",", ".").replace(" ", "");
									if(strClosingAmount.indexOf(".") + 1 == strClosingAmount.length())
										strClosingAmount += "00";
									else if(!schedulerUtil.IsNumeric(strClosingAmount.substring(strClosingAmount.indexOf(".") + 1)))
										strClosingAmount = strClosingAmount.substring(strClosingAmount.indexOf(".")) + ".00";
									//if(lstRecord.get(intPosition).indexOf(":62M") > -1)
									if(strData.indexOf(":62M") > -1)
									{
										objType950.setClosingMiddleAmount(strClosingAmount);
										objType950.setClType(0);
									}
									else
									{
										objType950.setClosingAmount(strClosingAmount);
										objType950.setClType(1);
									}
									//end set closing balance details
									
									String strTempBankDetails = "";
									if(strStatLine.length() >= 16 &&
										objType61Data.getMTRefno().trim().equalsIgnoreCase(""))
										strTempBankDetails = strStatLine.substring(strStatLine.length() - 16);
									objType61Data.setBankDetails(strTempBankDetails.trim());
									break;
								}
								else
								{
									if(strData.indexOf(":61:") > -1)
									{
										strData = "";
										i -= 1;
									}
									
									String strTempBankDetails = "";
									if(strStatLine.length() >= 16 &&
										objType61Data.getMTRefno().trim().equalsIgnoreCase(""))
										strTempBankDetails = strStatLine.substring(strStatLine.length() - 16);
									objType61Data.setBankDetails(strTempBankDetails.trim() + " " + strData);
								}
							}
						}
						else
						{
							strData = lstRecord.get(i);
							if(strData.indexOf(":62") == 0)
							{
								//set closing balance details
								int intTempPos = strData.indexOf(":62") + 5;
								
								//Fetch Transaction Type (C,D,RD,RC (opening balance))
								objType950.setClosingTranType(strData.substring(intTempPos, intTempPos + 1));
								
								intTempPos += 1;
								//Set Value Date (opening balance)
								objType950.setClosingValueDate(strData.substring(intTempPos, intTempPos + 6));
								
								intTempPos += 6;
								//Set currency code (opening balance)
								objType950.setClosingCurrCode(strData.substring(intTempPos, intTempPos + 3));
								
								intTempPos += 3;
								//set amount (opening balance)
								String strClosingAmount = strData.substring(intTempPos);
								strClosingAmount = strClosingAmount.replace(",", ".").replace(" ", "");
								if(strClosingAmount.indexOf(".") + 1 == strClosingAmount.length())
									strClosingAmount += "00";
								else if(!schedulerUtil.IsNumeric(strClosingAmount.substring(strClosingAmount.indexOf(".") + 1)))
									strClosingAmount = strClosingAmount.substring(strClosingAmount.indexOf(".")) + ".00";
								//if(lstRecord.get(intPosition).indexOf(":62M") > -1)
								if(strData.indexOf(":62M") > -1)
								{
									objType950.setClosingMiddleAmount(strClosingAmount);
									objType950.setClType(0);
								}
								else
								{
									objType950.setClosingAmount(strClosingAmount);
									objType950.setClType(1);
								}
								//end set closing balance details
							}
						}
					}
				}
				else
				{
					//CurrCode, Amount
					strData = ":62";
					objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
					intPosition = objRetVal.intRetVal;
					strData = objRetVal.strRetVal;
					if(intPosition > -1)
					{
						strData = lstRecord.get(intPosition);
						if(strData.indexOf(":62") == 0)
						{
							//set closing balance details
							int intTempPos = strData.indexOf(":62") + 5;
							
							//Fetch Transaction Type (C,D,RD,RC (opening balance))
							objType950.setClosingTranType(strData.substring(intTempPos, intTempPos + 1));
							
							intTempPos += 1;
							//Set Value Date (opening balance)
							objType950.setClosingValueDate(strData.substring(intTempPos, intTempPos + 6));
							
							intTempPos += 6;
							//Set currency code (opening balance)
							objType950.setClosingCurrCode(strData.substring(intTempPos, intTempPos + 3));
							
							intTempPos += 3;
							//set amount (opening balance)
							String strClosingAmount = strData.substring(intTempPos);
							strClosingAmount = strClosingAmount.replace(",", ".").replace(" ", "");
							if(strClosingAmount.indexOf(".") + 1 == strClosingAmount.length())
								strClosingAmount += "00";
							else if(!schedulerUtil.IsNumeric(strClosingAmount.substring(strClosingAmount.indexOf(".") + 1)))
								strClosingAmount = strClosingAmount.substring(strClosingAmount.indexOf(".")) + ".00";
							//if(lstRecord.get(intPosition).indexOf(":62M") > -1)
							if(strData.indexOf(":62M") > -1)
							{
								objType950.setClosingMiddleAmount(strClosingAmount);
								objType950.setClType(0);
							}
							else
							{
								objType950.setClosingAmount(strClosingAmount);
								objType950.setClType(1);
							}
							//end set closing balance details
						}
					}
				}
				
				//----------------------Closing Available Balance (Available Funds)-------
				strData = ":64";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
				{
					//int intTempPos = lstRecord.get(intPosition).indexOf(":64") + 4;
					int intTempPos = strData.indexOf(":64") + 4;
					
					//Fetch Transaction Type (C,D (Available Funds))
					objType950.setClosingAvalBalTranType(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 1));
					
					intTempPos += 1;
					//Set Value Date (Available Funds)
					objType950.setClosingAvalBalDate(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 6));
					
					intTempPos += 6;
					//Set currency code (Available Funds)
					objType950.setClosingAvalBalCurrCode(lstRecord.get(intPosition).substring(intTempPos, intTempPos + 3));
					
					intTempPos += 3;
					//set amount (Available Funds)
					String strAmount = lstRecord.get(intPosition).substring(intTempPos);
					strAmount = strAmount.replace(",", ".").replace(" ", "");
					if(strAmount.indexOf(".") + 1 == strAmount.length())
						strAmount += "00";
					else if(!schedulerUtil.IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1)))
						strAmount = strAmount.substring(strAmount.indexOf(".")) + ".00";
					if(lstRecord.get(intPosition).indexOf(":64") > -1)
					{
						objType950.setClosingAvalBalAmount(strAmount);
		    		}
				}
				
				//----------------------Forward Available Balance-------
				strData = ":65";
				objRetVal = schedulerUtil.fnFindByTagsDescMultiple(lstTags.get(Current), strData);
				if(objRetVal.intRetValArr != null)
				{
					if(objRetVal.intRetValArr.length > 0 )
					{
						String strForwardAvailableBalanceTemp = "";
						for(int i = 0; i < objRetVal.intRetValArr.length; i++)
						{
							intPosition = objRetVal.intRetValArr[i];
							strData = objRetVal.strRetValArr[i];
							if(intPosition > -1)
							{
								if(("").equalsIgnoreCase(strForwardAvailableBalanceTemp))
								{
									strForwardAvailableBalanceTemp = schedulerUtil.CustomTags(lstRecord.get(intPosition), strData);
								}
								else
								{
									strForwardAvailableBalanceTemp = strForwardAvailableBalanceTemp + "|" + schedulerUtil.CustomTags(lstRecord.get(intPosition), strData);
								}
							}
						}
						objType950.setForwardAvailableBalance(strForwardAvailableBalanceTemp.replaceAll(",", "."));
					}
				}
				
				Current++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SwiftParser9-UploadMT950", "");
		}
		return lstType950;
	}
	
	private Object UploadMT999(String FilePath, Integer ActivityCode) throws IOException
	{
		ArrayList<Hashtable<String, Integer>> lstTags = new ArrayList<Hashtable<String,Integer>>();
		ArrayList<ArrayList<String>> lstRecords = schedulerUtil.fnReadFileIntoList(FilePath, lstTags);
		ArrayList<String> completeMsgList = schedulerUtil.getMessageString(FilePath);
		Integer intPosition;
		Integer Current = 0;
		String strData;
		Type999 objType999;
		ArrayList<Type999> lstType999 = new ArrayList<Type999>();
		ReturnValue objRetVal = new ReturnValue();
		
		try
		{
			for (ArrayList<String> lstRecord : lstRecords)
			{
				lstType999.add(new Type999());
				objType999 = lstType999.get(Current);
				objType999.setActivityCode(String.valueOf(ActivityCode));
				
				String completeMsgStr = completeMsgList.get(Current);
				objType999.setCompleteMsgStr(completeMsgStr);
				
				String inwardOutwardType = schedulerUtil.GetInwardOutwardType(lstRecord.get(0));
				String senderCode = "";
				String recCode = "";
				
				if(("2").equalsIgnoreCase(inwardOutwardType))
				{
					 senderCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
					 recCode = schedulerUtil.SenderCode(lstRecord.get(0)).substring(0,8);
				}
				else
				{
					 senderCode = schedulerUtil.SenderCode(lstRecord.get(0));
					 recCode = schedulerUtil.ReceiverBIC(lstRecord.get(0));
				}
								
				//inwardOutwardType
				if(lstRecord.size() > 0)
					objType999.setInwardOutwardType(inwardOutwardType);
				
				//Sender Code
				if(lstRecord.size() > 0)
					objType999.setSenderCode(senderCode);

				//Receiver BIC
				if(lstRecord.size() > 0)
					objType999.setReceiverBIC(recCode);
					
					// ACK/NACK
				if(lstRecord.size() > 0)
					objType999.setAckNackedFlag(schedulerUtil.GetAckNackedFlag(lstRecord.get(0)));
				
				
				//TRF No
				if(lstTags.get(Current).containsKey(":20:"))
				{
					objType999.setTransRefNo(schedulerUtil.TRFNo(lstRecord.get(lstTags.get(Current).get(":20:"))));
				}
				
				//RelRef
				strData = ":21:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
				{
					objType999.setRelRefNo(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData));
				}
				
				//79
				strData = ":79:";
				objRetVal = schedulerUtil.fnFindByTagsDesc(lstTags.get(Current), strData);
				intPosition = objRetVal.intRetVal;
				strData = objRetVal.strRetVal;
				if(intPosition > -1)
				{ 
					objType999.setNarrative(schedulerUtil.CustomTags(lstRecord.get(intPosition), strData) + 
							schedulerUtil.fnGetCustomDataLineSeperator(lstRecord, intPosition, 34).strRetVal);
				}
				Current++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)Department SwiftParser-UploadMT999", "");
		}
		return lstType999;
	}
	
	private Integer fnGetActivityCode(MsgType9 msgType)
	{
		switch(msgType)
		{
			case MT910:
				return 910;
			case MT940:
				return 940;
			case MT950:
				return 950;
			case MT999:
				return 999;
			default:
				return -1;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public int GetInsertStatement(Integer intRecordID, MsgType9 msgType, Object objOutput) 
	{
		int errorCode = -1;
		String strTempData = "";
		
		try
		{
			switch(msgType)
			{
				case MT910:
					ArrayList<Type910> lstType910 = (ArrayList<Type910>)objOutput;
					strTempData = "BEGIN\n";
					if(lstType910.size() > 0)
					{
						int intRowID = 0;
						for (Type910 type910 : lstType910) 
						{
							intRowID = intRowID + 1;
							strTempData += "INSERT INTO ARM_TMP_910 VALUES(";
							strTempData += String.valueOf(intRecordID) +
								"," + String.valueOf(intRowID) + ",'" + type910.getTRFNo().replaceAll("'", "''") + "','" +
								type910.getValDate().replaceAll("'", "''") + "','" + type910.getAmount().replaceAll("'", "''")
								+ "','" + type910.getCurrCode().replaceAll("'", "''") + "','" + type910.getSenderCode().replaceAll("'", "''")
								+ "','" + type910.getRelRef().replaceAll("'", "''") + "','" + type910.getAccountID().replaceAll("'", "''")
								+ "','" + type910.getOrdCustInf().replaceAll("'", "''") + "','" + type910.getOrdInst().replaceAll("'", "''")
								+ "','" + type910.getIntermed().replaceAll("'", "''") + "','" + type910.getSenderInfo().replaceAll("'", "''")
								 + "','" + type910.getReceiverBIC() + "','" + type910.getInwardOutwardType() +  "','" + type910.getAckNackedFlag() + "');\n";
							
							strTempData += "INSERT INTO ARM_TMP_UPL_COMPLETE_MSG VALUES(";
							strTempData += String.valueOf(intRecordID) + 
								"," + String.valueOf(intRowID) + 
								",'" + type910.getCompleteMsgStr().replaceAll("'", "''") + "');\n";
						}
					}
					strTempData += "\n EXEC STP_UPL_PROCESS_JOB_910 " + String.valueOf(intRecordID) + ";\nEND;";
					errorCode = ameyaSwiftMsgsService.executeInsertStatement(strTempData);
					return errorCode;
				case MT940:
					ArrayList<Type940> lstType940 = (ArrayList<Type940>)objOutput;
					if(lstType940.size() > 0)
					{
						strTempData = "BEGIN\n";
						int intRowID = 0;
						for (Type940 type940 : lstType940) 
						{
							if(lstType940.size() > 0 && type940.lstType61Data.size() > 0 )
							{
								for (Type61Data type61Data : type940.lstType61Data) 
								{
									intRowID = intRowID + 1;
									strTempData += "INSERT INTO TBL_TMP_940 VALUES(";
									strTempData += String.valueOf(intRecordID) +                                       // 1. UPLOAD_KEY
									    "," + String.valueOf(intRowID) +                                                // 2. ROW_NUMBER
									    ",'" + type940.getCurrCode().replaceAll("'", "''") + "'" +                     // 3. CURR_CODE
									    ",'" + type61Data.getTranType().replaceAll("'", "''") + "'" +                  // 4. TRAN_TYPE
									    ",'" + type61Data.getDealAmount().replaceAll("'", "''") + "'" +                // 5. DEAL_AMOUNT
									    ",'" + type61Data.getMTRefno().replaceAll("'", "''") + "'" +                   // 6. MT_REF_NO
									    ",'" + type61Data.getValDate().replaceAll("'", "''") + "'" +                   // 7. VAL_DATE
									    ",'" + type61Data.getBankDetails().replaceAll("'", "''") + "'" +               // 8. BANK_DETAILS
									    ",'" + type940.getSenderCode().replaceAll("'", "''") + "'" +                   // 9. SENDER_CODE
									    ",'" + type61Data.getTranRefNo().replaceAll("'", "''") + "'" +                 // 10. TRAN_REF_NO
									    ",'" + type61Data.getField86Val().replaceAll("'", "''") + "'" +                // 11. FIELD_86_VAL
									    ",'" + type61Data.getStatLine().replaceAll("'", "''") + "'" +                  // 12. STAT_LINE
									    ",'" + type940.getAccountID().replaceAll("'", "''") + "'" +                    // 13. ACCOUNT_ID
									    ",'" + type940.getReceiverBIC() + "'" +                                         // 14. REC_BIC
									    "," + String.valueOf(type940.getMessageNo()) +                                  // 15. GROUP_NO
									    ",'" + type940.getTranType().replaceAll("'", "''") + "'" +                     // 16. OP_TRAN_TYPE
									    ",'" + type940.getValueDate().replaceAll("'", "''") + "'" +                    // 17. OP_DATE
									    ",'" + type940.getCurrCode().replaceAll("'", "''") + "'" +                     // 18. OP_CURR_CODE
									    ",'" + type940.getAmount().replaceAll("'", "''") + "'" +                       // 19. OP_AMOUNT
									    ",'" + type940.getClosingTranType().replaceAll("'", "''") + "'" +              // 20. CL_TRAN_TYPE
									    ",'" + type940.getClosingValueDate().replaceAll("'", "''") + "'" +             // 21. CL_DATE
									    ",'" + type940.getClosingCurrCode().replaceAll("'", "''") + "'" +              // 22. CL_CURR_CODE
									    ",'" + type940.getClosingAmount().replaceAll("'", "''") + "'" +                // 23. CL_AMOUNT
									    ",'" + type940.getMiddleAmount().replaceAll("'", "''") + "'" +                 // 24. OP_M_AMOUNT
									    ",'" + type940.getClosingMiddleAmount().replaceAll("'", "''") + "'" +          // 25. CL_M_AMOUNT
									    "," + String.valueOf(type940.getOpType()) +                                     // 26. OP_TYPE
									    "," + String.valueOf(type940.getClType()) +                                     // 27. CL_TYPE
									    ",'" + type940.getStatOrSeq().replaceAll("'", "''") + "'" +                    // 28. STMT_SEQ
									    ",'" + type61Data.getEntryDate().replaceAll("'", "''") + "'" +                 // 29. ENTRY_DATE
									    ",'" + type940.getClosingAvalBalTranType().replaceAll("'", "''") + "'" +       // 30. CL_AVAL_TRAN_TYPE
									    ",'" + type940.getClosingAvalBalDate().replaceAll("'", "''") + "'" +           // 31. CL_AVAL_DATE
									    ",'" + type940.getClosingAvalBalCurrCode().replaceAll("'", "''") + "'" +       // 32. CL_AVAL_CURR_CODE
									    ",'" + type940.getClosingAvalBalAmount().replaceAll("'", "''") + "'" +         // 33. CL_AVAL_AMOUNT
									    "," + type940.getInwardOutwardType() +                                          // 34. IN_OUT_FLAG
									    "," + type940.getAckNackedFlag() +                                              // 35. ACK_NACK_FLAG
									    ",'" + type61Data.getTransTypeIdentificationCode().replaceAll("'", "''") + "'" + // 36. TRANS_TYPE_IDENTIFI_CODE
									    ",'" + type940.getForwardAvailableBalance().replaceAll("'", "''") + "'" +      // 37. FORWARD_AVAILABLE_BAL
									    ",NULL" +                                                                        // 38. CURRENCY_KEY
									    ",NULL" +                                                                        // 39. CL_CURRENCY_KEY
									    ",NULL" +                                                                        // 40. OP_CURRENCY_KEY
									    ",NULL" +                                                                        // 41. CL_AVAL_CURRENCY_KEY
									    ",NULL" +                                                                        // 42. BRANCH_CODE
									    ",NULL" +                                                                        // 43. BRANCH_KEY
									    ",NULL" +                                                                        // 44. CCAT_BRANCH_CODE
									    ",NULL" +                                                                        // 45. ERROR_REMARKS
									    ",NULL" +                                                                        // 46. HDR_TRAN_REF_NO
									    ",'" + type940.getRelRef().replaceAll("'", "''") + "'" +                       // 47. HDR_REL_REF_NO
									    ",NULL" +                                                                        // 48. HDR_VAL_DATE
									    ",NULL" +                                                                        // 49. HDR_CURR_CODE
									    ",NULL" +                                                                        // 50. HDR_AMOUNT
									    ",NULL" +                                                                        // 51. HDR_SENDER_CODE
									    ",NULL" +                                                                        // 52. HDR_REC_BIC
									    ",NULL" +                                                                        // 53. APPLICATION_ID
									    ",NULL" +                                                                        // 54. SERVICE_ID
									    ",NULL" +                                                                        // 55. SESSION_NUMBER1
									    ",NULL" +                                                                        // 56. SEQUENCE_NUMBER1
									    ",NULL" +                                                                        // 57. INPUT_TIME
									    ",NULL" +                                                                        // 58. HEADER_DATE
									    ",NULL" +                                                                        // 59. SESSION_NUMBER2
									    ",NULL" +                                                                        // 60. SEQUENCE_NUMBER2
									    ",NULL" +                                                                        // 61. OUTPUT_DATE
									    ",NULL" +                                                                        // 62. OUTPUT_TIME
									    ",NULL" +                                                                        // 63. BANK_PRIORITY_CODE
									    ",NULL" +                                                                        // 64. HEADER_BLOCK3
									    ",'" + type940.getTRFNo().replaceAll("'", "''") + "'" +                        // 65. MUR_NUMBER
									    ",NULL" +                                                                        // 66. HDR_108_DATE
									    ",NULL" +                                                                        // 67. UUID
									    ");\n";

								}
							}
							else if(lstType940.size() > 0 && type940.lstType61Data.size() == 0)
							{
								intRowID = intRowID + 1;
								strTempData += "INSERT INTO TBL_TMP_940_950_HEADER VALUES(";
								strTempData += String.valueOf(intRecordID) +                                    // 1. UPLOAD_KEY
								        "," + String.valueOf(intRowID) +                                             // 2. ROW_NUMBER
								        ",'" + type940.getCurrCode().replaceAll("'", "''") + "'" +                  // 3. CURR_CODE
								        ",NULL" +                                                                     // 4. TRAN_TYPE
								        ",NULL" +                                                                     // 5. DEAL_AMOUNT
								        ",NULL" +                                                                     // 6. MT_REF_NO
								        ",NULL" +                                                                     // 7. VAL_DATE
								        ",NULL" +                                                                     // 8. BANK_DETAILS
								        ",'" + type940.getSenderCode().replaceAll("'", "''") + "'" +                // 9. SENDER_CODE
								        ",NULL" +                                                                     // 10. TRAN_REF_NO
								        ",NULL" +                                                                     // 11. FIELD_86_VAL
								        ",NULL" +                                                                     // 12. STAT_LINE
								        ",'" + type940.getAccountID().replaceAll("'", "''") + "'" +                 // 13. ACCOUNT_ID
								        ",'" + type940.getReceiverBIC() + "'" +                                      // 14. REC_BIC
								        "," + String.valueOf(type940.getMessageNo()) +                               // 15. GROUP_NO
								        ",'" + type940.getTranType().replaceAll("'", "''") + "'" +                  // 16. OP_TRAN_TYPE
								        ",'" + type940.getValueDate().replaceAll("'", "''") + "'" +                 // 17. OP_DATE
								        ",'" + type940.getCurrCode().replaceAll("'", "''") + "'" +                  // 18. OP_CURR_CODE
								        ",'" + type940.getAmount().replaceAll("'", "''") + "'" +                    // 19. OP_AMOUNT
								        ",'" + type940.getClosingTranType().replaceAll("'", "''") + "'" +           // 20. CL_TRAN_TYPE
								        ",'" + type940.getClosingValueDate().replaceAll("'", "''") + "'" +          // 21. CL_DATE
								        ",'" + type940.getClosingCurrCode().replaceAll("'", "''") + "'" +           // 22. CL_CURR_CODE
								        ",'" + type940.getClosingAmount().replaceAll("'", "''") + "'" +             // 23. CL_AMOUNT
								        ",'" + type940.getMiddleAmount().replaceAll("'", "''") + "'" +              // 24. OP_M_AMOUNT
								        ",'" + type940.getClosingMiddleAmount().replaceAll("'", "''") + "'" +       // 25. CL_M_AMOUNT
								        "," + String.valueOf(type940.getOpType()) +                                  // 26. OP_TYPE
								        "," + String.valueOf(type940.getClType()) +                                  // 27. CL_TYPE
								        ",'" + type940.getStatOrSeq().replaceAll("'", "''") + "'" +                 // 28. STMT_SEQ
								        ",NULL" +                                                                     // 29. ENTRY_DATE
								        ",'" + type940.getClosingAvalBalTranType().replaceAll("'", "''") + "'" +    // 30. CL_AVAL_TRAN_TYPE
								        ",'" + type940.getClosingAvalBalDate().replaceAll("'", "''") + "'" +        // 31. CL_AVAL_DATE
								        ",'" + type940.getClosingAvalBalCurrCode().replaceAll("'", "''") + "'" +    // 32. CL_AVAL_CURR_CODE
								        ",'" + type940.getClosingAvalBalAmount().replaceAll("'", "''") + "'" +      // 33. CL_AVAL_AMOUNT
								        ",'" + type940.getForwardAvailableBalance().replaceAll("'", "''") + "'" +   // 34. FORWARD_AVAILABLE_BAL
								        ",NULL" +                                                                     // 35. CURRENCY_KEY
								        ",NULL" +                                                                     // 36. CL_CURRENCY_KEY
								        ",NULL" +                                                                     // 37. OP_CURRENCY_KEY
								        ",NULL" +                                                                     // 38. CL_AVAL_CURRENCY_KEY
								        ",NULL" +                                                                     // 39. ERROR_REMARKS
								        ",NULL" +                                                                     // 40. IN_OUT_FLAG
								        ",NULL" +                                                                     // 41. HDR_TRAN_REF_NO
								        ",NULL" +                                                                     // 42. HDR_REL_REF_NO
								        ",NULL" +                                                                     // 43. HDR_VAL_DATE
								        ",NULL" +                                                                     // 44. HDR_CURR_CODE
								        ",NULL" +                                                                     // 45. HDR_AMOUNT
								        ",NULL" +                                                                     // 46. HDR_SENDER_CODE
								        ",NULL" +                                                                     // 47. HDR_REC_BIC
								        ",NULL" +                                                                     // 48. APPLICATION_ID
								        ",NULL" +                                                                     // 49. SERVICE_ID
								        ",NULL" +                                                                     // 50. SESSION_NUMBER1
								        ",NULL" +                                                                     // 51. SEQUENCE_NUMBER1
								        ",NULL" +                                                                     // 52. INPUT_TIME
								        ",NULL" +                                                                     // 53. HEADER_DATE
								        ",NULL" +                                                                     // 54. SESSION_NUMBER2
								        ",NULL" +                                                                     // 55. SEQUENCE_NUMBER2
								        ",NULL" +                                                                     // 56. OUTPUT_DATE
								        ",NULL" +                                                                     // 57. OUTPUT_TIME
								        ",NULL" +                                                                     // 58. BANK_PRIORITY_CODE
								        ",NULL" +                                                                     // 59. HEADER_BLOCK3
								        ",'" + type940.getTRFNo().replaceAll("'", "''") + "'" +                     // 60. MUR_NUMBER
								        ",NULL" +                                                                     // 61. HDR_108_DATE
								        ",NULL" +                                                                     // 62. UUID
								        ");\n";
							}
							
//							strTempData += "INSERT INTO ARM_TMP_UPL_COMPLETE_MSG VALUES(";
//							strTempData += String.valueOf(intRecordID) + 
//								"," + String.valueOf(intRowID) + 
//								",'" + type940.getCompleteMsgStr().replaceAll("'", "''") + "');\n";
						}
						//strTempData += "\n EXEC STP_UPL_PROCESS_JOB_940 " + String.valueOf(intRecordID) + ";\nEND;";
						strTempData += "  COMMIT;\n";
						strTempData += "  STP_UPL_PROCESS_JOB_940(" + String.valueOf(intRecordID) + ");\n";
						
				        strTempData += "END;";
					}
					errorCode = ameyaSwiftMsgsService.executeInsertStatement(strTempData);
					return errorCode;
				case MT950:
					ArrayList<Type950> lstType950 = (ArrayList<Type950>)objOutput;
					
					if(lstType950.size() > 0)
					{
						strTempData = "BEGIN\n";
						int intRowID = 0;
						for (Type950 type950 : lstType950) 
						{
							if(lstType950.size() > 0 && type950.lstType61Data.size() > 0 )
							{
								for (Type61Data type61Data : type950.lstType61Data) 
								{
									intRowID = intRowID + 1;
									strTempData += "  INSERT INTO TBL_TMP_950 VALUES(";
				                    strTempData += String.valueOf(intRecordID) +                                    // 1. UPLOAD_KEY
				                        "," + String.valueOf(intRowID) +                                             // 2. ROW_NUMBER
				                        ",'" + type950.getCurrCode().replaceAll("'", "''") + "'" +                  // 3. CURR_CODE
				                        ",'" + type61Data.getTranType().replaceAll("'", "''") + "'" +               // 4. TRAN_TYPE
				                        ",'" + type61Data.getDealAmount().replaceAll("'", "''") + "'" +             // 5. DEAL_AMOUNT
				                        ",'" + type61Data.getMTRefno().replaceAll("'", "''") + "'" +                // 6. MT_REF_NO
				                        ",'" + type61Data.getValDate().replaceAll("'", "''") + "'" +                // 7. VAL_DATE
				                        ",'" + type61Data.getBankDetails().replaceAll("'", "''") + "'" +            // 8. BANK_DETAILS
				                        ",'" + type950.getSenderCode().replaceAll("'", "''") + "'" +                // 9. SENDER_CODE
				                        ",'" + type61Data.getTranRefNo().replaceAll("'", "''") + "'" +              // 10. TRAN_REF_NO
				                        ",'" + type61Data.getStatLine().replaceAll("'", "''") + "'" +               // 11. STAT_LINE
				                        ",'" + type950.getAccountID().replaceAll("'", "''") + "'" +                 // 12. ACCOUNT_ID
				                        ",'" + type950.getReceiverBIC() + "'" +                                      // 13. REC_BIC
				                        "," + String.valueOf(type950.getMessageNo()) +                               // 14. GROUP_NO
				                        ",'" + type950.getTranType().replaceAll("'", "''") + "'" +                  // 15. OP_TRAN_TYPE
				                        ",'" + type950.getValueDate().replaceAll("'", "''") + "'" +                 // 16. OP_DATE
				                        ",'" + type950.getCurrCode().replaceAll("'", "''") + "'" +                  // 17. OP_CURR_CODE
				                        ",'" + type950.getAmount().replaceAll("'", "''") + "'" +                    // 18. OP_AMOUNT
				                        ",'" + type950.getClosingTranType().replaceAll("'", "''") + "'" +           // 19. CL_TRAN_TYPE
				                        ",'" + type950.getClosingValueDate().replaceAll("'", "''") + "'" +          // 20. CL_DATE
				                        ",'" + type950.getClosingCurrCode().replaceAll("'", "''") + "'" +           // 21. CL_CURR_CODE
				                        ",'" + type950.getClosingAmount().replaceAll("'", "''") + "'" +             // 22. CL_AMOUNT
				                        ",'" + type950.getMiddleAmount().replaceAll("'", "''") + "'" +              // 23. OP_M_AMOUNT
				                        ",'" + type950.getClosingMiddleAmount().replaceAll("'", "''") + "'" +       // 24. CL_M_AMOUNT
				                        "," + String.valueOf(type950.getOpType()) +                                  // 25. OP_TYPE
				                        "," + String.valueOf(type950.getClType()) +                                  // 26. CL_TYPE
				                        ",'" + type950.getStatOrSeq().replaceAll("'", "''") + "'" +                 // 27. STMT_SEQ
				                        ",'" + type61Data.getEntryDate().replaceAll("'", "''") + "'" +              // 28. ENTRY_DATE
				                        ",'" + type950.getClosingAvalBalTranType().replaceAll("'", "''") + "'" +    // 29. CL_AVAL_TRAN_TYPE
				                        ",'" + type950.getClosingAvalBalDate().replaceAll("'", "''") + "'" +        // 30. CL_AVAL_DATE
				                        ",'" + type950.getClosingAvalBalCurrCode().replaceAll("'", "''") + "'" +    // 31. CL_AVAL_CURR_CODE
				                        ",'" + type950.getClosingAvalBalAmount().replaceAll("'", "''") + "'" +      // 32. CL_AVAL_AMOUNT
				                        "," + type950.getInwardOutwardType() +                                       // 33. IN_OUT_FLAG
				                        "," + type950.getAckNackedFlag() +                                           // 34. ACK_NACK_FLAG
				                        ",'" + type61Data.getTransTypeIdentificationCode().replaceAll("'", "''") + "'" +                   // 35. TRANS_TYPE_IDENTIFI_CODE
				                        ",'" + type950.getForwardAvailableBalance().replaceAll("'", "''") + "'" +                          // 36. FORWARD_AVAILABLE_BAL
				                        ",NULL" +                                                                     // 37. CURRENCY_KEY
				                        ",NULL" +                                                                     // 38. CL_CURRENCY_KEY
				                        ",NULL" +                                                                     // 39. OP_CURRENCY_KEY
				                        ",NULL" +                                                                     // 40. CL_AVAL_CURRENCY_KEY
				                        ",NULL" +                                                                     // 41. BRANCH_CODE
				                        ",NULL" +                                                                     // 42. BRANCH_KEY
				                        ",NULL" +                                                                     // 43. CCAT_BRANCH_CODE
				                        ",NULL" +                                                                     // 44. ERROR_REMARKS
				                        ",NULL" +                                                                     // 45. HDR_TRAN_REF_NO
				                        ",NULL" +                                                                     // 46. HDR_REL_REF_NO
				                        ",NULL" +                                                                     // 47. HDR_VAL_DATE
				                        ",NULL" +                                                                     // 48. HDR_CURR_CODE
				                        ",NULL" +                                                                     // 49. HDR_AMOUNT
				                        ",NULL" +                                                                     // 50. HDR_SENDER_CODE
				                        ",NULL" +                                                                     // 51. HDR_REC_BIC
				                        ",NULL" +                                                                     // 52. APPLICATION_ID
				                        ",NULL" +                                                                     // 53. SERVICE_ID
				                        ",NULL" +                                                                     // 54. SESSION_NUMBER1
				                        ",NULL" +                                                                     // 55. SEQUENCE_NUMBER1
				                        ",NULL" +                                                                     // 56. INPUT_TIME
				                        ",NULL" +                                                                     // 57. HEADER_DATE
				                        ",NULL" +                                                                     // 58. SESSION_NUMBER2
				                        ",NULL" +                                                                     // 59. SEQUENCE_NUMBER2
				                        ",NULL" +                                                                     // 60. OUTPUT_DATE
				                        ",NULL" +                                                                     // 61. OUTPUT_TIME
				                        ",NULL" +                                                                     // 62. BANK_PRIORITY_CODE
				                        ",NULL" +                                                                     // 63. HEADER_BLOCK3
				                        ",'" + type950.getTRFNo() + "'" +                                            // 64. MUR_NUMBER
				                        ",NULL" +                                                                     // 65. HDR_108_DATE
				                        ",NULL" +                                                                     // 66. UUID
				                        ");\n";

								}
							}
							else if(lstType950.size() > 0 && type950.lstType61Data.size() == 0)
							{
								intRowID = intRowID + 1;
								strTempData += "INSERT INTO TBL_TMP_940_950_HEADER VALUES(";
//								strTempData += String.valueOf(intRecordID) +
//									"," + String.valueOf(intRowID) + ",'" + type950.getCurrCode().replaceAll("'", "''") + "','" +
//									"" + "','" + ""
//									+ "','" + "" + "','" + ""
//									+ "','" + "" + "','" + type950.getSenderCode().replaceAll("'", "''")
//									+ "','" + "" + "','" + "','"
//									+ "','" + type950.getAccountID().replaceAll("'", "''") + "','" + type950.getReceiverBIC() + "'," + String.valueOf(type950.getMessageNo())
//									+ ",'"  + type950.getTranType().replaceAll("'", "''") + "','" + type950.getValueDate().replaceAll("'", "''") 
//									+ "','" + type950.getCurrCode().replaceAll("'", "''") + "','" + type950.getAmount().replaceAll("'", "''")
//									+ "','" + type950.getClosingTranType().replaceAll("'", "''") + "','" + type950.getClosingValueDate().replaceAll("'", "''") 
//									+ "','" + type950.getClosingCurrCode().replaceAll("'", "''") + "','" + type950.getClosingAmount().replaceAll("'", "''")  
//									+ "','" + type950.getMiddleAmount().replaceAll("'", "''") + "','" + type950.getClosingMiddleAmount().replaceAll("'", "''")
//									+ "'," + String.valueOf(type950.getOpType()) + "," + String.valueOf(type950.getClType())
//									+ ",'" + type950.getStatOrSeq().replaceAll("'", "''") 
//									+ "','" + "" 
//									+ "','" + type950.getClosingAvalBalTranType().replaceAll("'", "''") 
//									+ "','" + type950.getClosingAvalBalDate().replaceAll("'", "''")
//									+ "','" + type950.getClosingAvalBalCurrCode().replaceAll("'", "''") 
//									+ "','" + type950.getClosingAvalBalAmount().replaceAll("'", "''")
//									+ "','" + type950.getTRFNo().replaceAll("'", "''")
//									+ "','" + "" 
//									+ "','" + type950.getForwardAvailableBalance()	
//									/*+ "','" +type950.getInwardOutwardType()*/
//									+ "');\n";
								strTempData += String.valueOf(intRecordID) +                                    // 1. UPLOAD_KEY
								        "," + String.valueOf(intRowID) +                                             // 2. ROW_NUMBER
								        ",'" + type950.getCurrCode().replaceAll("'", "''") + "'" +                  // 3. CURR_CODE
								        ",NULL" +                                                                     // 4. TRAN_TYPE
								        ",NULL" +                                                                     // 5. DEAL_AMOUNT
								        ",NULL" +                                                                     // 6. MT_REF_NO
								        ",NULL" +                                                                     // 7. VAL_DATE
								        ",NULL" +                                                                     // 8. BANK_DETAILS
								        ",'" + type950.getSenderCode().replaceAll("'", "''") + "'" +                // 9. SENDER_CODE
								        ",NULL" +                                                                     // 10. TRAN_REF_NO
								        ",NULL" +                                                                     // 11. FIELD_86_VAL
								        ",NULL" +                                                                     // 12. STAT_LINE
								        ",'" + type950.getAccountID().replaceAll("'", "''") + "'" +                 // 13. ACCOUNT_ID
								        ",'" + type950.getReceiverBIC() + "'" +                                      // 14. REC_BIC
								        "," + String.valueOf(type950.getMessageNo()) +                               // 15. GROUP_NO
								        ",'" + type950.getTranType().replaceAll("'", "''") + "'" +                  // 16. OP_TRAN_TYPE
								        ",'" + type950.getValueDate().replaceAll("'", "''") + "'" +                 // 17. OP_DATE
								        ",'" + type950.getCurrCode().replaceAll("'", "''") + "'" +                  // 18. OP_CURR_CODE
								        ",'" + type950.getAmount().replaceAll("'", "''") + "'" +                    // 19. OP_AMOUNT
								        ",'" + type950.getClosingTranType().replaceAll("'", "''") + "'" +           // 20. CL_TRAN_TYPE
								        ",'" + type950.getClosingValueDate().replaceAll("'", "''") + "'" +          // 21. CL_DATE
								        ",'" + type950.getClosingCurrCode().replaceAll("'", "''") + "'" +           // 22. CL_CURR_CODE
								        ",'" + type950.getClosingAmount().replaceAll("'", "''") + "'" +             // 23. CL_AMOUNT
								        ",'" + type950.getMiddleAmount().replaceAll("'", "''") + "'" +              // 24. OP_M_AMOUNT
								        ",'" + type950.getClosingMiddleAmount().replaceAll("'", "''") + "'" +       // 25. CL_M_AMOUNT
								        "," + String.valueOf(type950.getOpType()) +                                  // 26. OP_TYPE
								        "," + String.valueOf(type950.getClType()) +                                  // 27. CL_TYPE
								        ",'" + type950.getStatOrSeq().replaceAll("'", "''") + "'" +                 // 28. STMT_SEQ
								        ",NULL" +                                                                     // 29. ENTRY_DATE
								        ",'" + type950.getClosingAvalBalTranType().replaceAll("'", "''") + "'" +    // 30. CL_AVAL_TRAN_TYPE
								        ",'" + type950.getClosingAvalBalDate().replaceAll("'", "''") + "'" +        // 31. CL_AVAL_DATE
								        ",'" + type950.getClosingAvalBalCurrCode().replaceAll("'", "''") + "'" +    // 32. CL_AVAL_CURR_CODE
								        ",'" + type950.getClosingAvalBalAmount().replaceAll("'", "''") + "'" +      // 33. CL_AVAL_AMOUNT
								        ",'" + type950.getForwardAvailableBalance().replaceAll("'", "''") + "'" +   // 34. FORWARD_AVAILABLE_BAL
								        ",NULL" +                                                                     // 35. CURRENCY_KEY
								        ",NULL" +                                                                     // 36. CL_CURRENCY_KEY
								        ",NULL" +                                                                     // 37. OP_CURRENCY_KEY
								        ",NULL" +                                                                     // 38. CL_AVAL_CURRENCY_KEY
								        ",NULL" +                                                                     // 39. ERROR_REMARKS
								        ",NULL" +                                                                     // 40. IN_OUT_FLAG
								        ",NULL" +                                                                     // 41. HDR_TRAN_REF_NO
								        ",NULL" +                                                                     // 42. HDR_REL_REF_NO
								        ",NULL" +                                                                     // 43. HDR_VAL_DATE
								        ",NULL" +                                                                     // 44. HDR_CURR_CODE
								        ",NULL" +                                                                     // 45. HDR_AMOUNT
								        ",NULL" +                                                                     // 46. HDR_SENDER_CODE
								        ",NULL" +                                                                     // 47. HDR_REC_BIC
								        ",NULL" +                                                                     // 48. APPLICATION_ID
								        ",NULL" +                                                                     // 49. SERVICE_ID
								        ",NULL" +                                                                     // 50. SESSION_NUMBER1
								        ",NULL" +                                                                     // 51. SEQUENCE_NUMBER1
								        ",NULL" +                                                                     // 52. INPUT_TIME
								        ",NULL" +                                                                     // 53. HEADER_DATE
								        ",NULL" +                                                                     // 54. SESSION_NUMBER2
								        ",NULL" +                                                                     // 55. SEQUENCE_NUMBER2
								        ",NULL" +                                                                     // 56. OUTPUT_DATE
								        ",NULL" +                                                                     // 57. OUTPUT_TIME
								        ",NULL" +                                                                     // 58. BANK_PRIORITY_CODE
								        ",NULL" +                                                                     // 59. HEADER_BLOCK3
								        ",'" + type950.getTRFNo().replaceAll("'", "''") + "'" +                     // 60. MUR_NUMBER
								        ",NULL" +                                                                     // 61. HDR_108_DATE
								        ",NULL" +                                                                     // 62. UUID
								        ");\n";
								
							}

//							strTempData += "INSERT INTO ARM_TMP_UPL_COMPLETE_MSG VALUES(";
//							strTempData += String.valueOf(intRecordID) + 
//								"," + String.valueOf(intRowID) + 
//								",'" + type950.getCompleteMsgStr().replaceAll("'", "''") + "');\n";

						}
					//	strTempData += "\n EXEC STP_UPL_PROCESS_JOB_950 " + String.valueOf(intRecordID) + ";\nEND;";
						strTempData += "  COMMIT;\n";
						strTempData += "  STP_UPL_PROCESS_JOB_950(" + String.valueOf(intRecordID) + ");\n";
						
				        strTempData += "END;";
					}
					System.out.println(strTempData);
					errorCode = ameyaSwiftMsgsService.executeInsertStatement(strTempData);
					return errorCode;	
					
				case MT999:
					ArrayList<Type999> lstType999 = (ArrayList<Type999>)objOutput;
					strTempData = "BEGIN\n";
					if(lstType999.size() > 0)
					{
						int intRowID = 0;
						for (Type999 type999 : lstType999) 
						{
							intRowID = intRowID + 1;
							strTempData += "INSERT INTO ARM_TMP_999 VALUES(";
							strTempData += String.valueOf(intRecordID) 
									+ "," + String.valueOf(intRowID) 
									+ ",'" + type999.getTransRefNo().replaceAll("'", "''") 
									+ "','" + type999.getRelRefNo().replaceAll("'", "''") 
									+ "','" + type999.getNarrative().replaceAll("'", "''")
									+ "','" + type999.getSenderCode().replaceAll("'", "''")
									+ "','" + type999.getReceiverBIC() 
									+ "','" + type999.getInwardOutwardType() 
									+  "','" + type999.getAckNackedFlag() + "');\n";
							
							strTempData += "INSERT INTO ARM_TMP_UPL_COMPLETE_MSG VALUES(";
							strTempData += String.valueOf(intRecordID) + 
								"," + String.valueOf(intRowID) + 
								",'" + type999.getCompleteMsgStr().replaceAll("'", "''") + "');\n";
						}
					}
					strTempData += "\n EXEC STP_UPL_PROCESS_JOB_999 " + String.valueOf(intRecordID) + ";\nEND;";
					errorCode = ameyaSwiftMsgsService.executeInsertStatement(strTempData);
					return errorCode;
				
			default:
				break;
			}
		}
		catch (Exception e) {
			errorCode = -1;
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SwiftParser9-GetInsertStatement", "");
		}
		return errorCode;
	}
}
