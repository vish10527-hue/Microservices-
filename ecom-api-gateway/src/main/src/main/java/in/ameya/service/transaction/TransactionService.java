package in.ameya.service.transaction;

import java.io.IOException;


import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.transaction.Transaction;


public interface TransactionService {

//	MessageDashboard getMessageDashboardCount(MessageDashboard messageDashboardObj);
//	
//    Transaction searchTransactions(Transaction transactionObj) ;
//
//	TransactionSearch geMessageTypeDetails(TransactionSearch transactionObj);
//
//	Type103 ge103MsgDetails(TransactionSearch transactionRequest);
//
//	Type202 ge202MsgDetails(TransactionSearch transactionRequest);
//	
	String exportTransactionCSV(Transaction transactionObj) throws IOException;
//
//	ExportResponse exportTransactionExcel(Transaction transactionObj) throws IOException;

	
	
	DBResponse getUploadedMsgDataList(Transaction transactionObj);
	
	DBResponse getReceivedMsgList(Transaction transactionObj);
	
	DBResponse getReceivedMsgView(String msgType, String msgTypeKey);
//
//	ExportResponse exportUploadedMsgDataExcel(Transaction transactionObj) throws IOException;
//
	String exportUploadedMsgDataCSV(Transaction transactionObj) throws IOException;
//	
//	ExportResponse exportTransactionPDF(Transaction transactionObj) throws IOException;



	String exportReceivedTransactionCSV(Transaction transactionObj);



	ExportResponse exportReceivedTransactionExcel(Transaction transactionObj) throws IOException;



	DBResponse getActivityDetail(Integer msgKey);
		
}
