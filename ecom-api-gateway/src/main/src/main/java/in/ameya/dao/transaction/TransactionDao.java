package in.ameya.dao.transaction;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.transaction.Transaction;


public interface TransactionDao{

//	MessageDashboard getMessageDashboardCount(MessageDashboard messageDashboardObj);
//	
//	Transaction searchTransactions(Transaction transactionObj);
//
//	TransactionSearch geMessageTypeDetails(TransactionSearch transactionObj);
//
//	Type103 ge103MsgDetails(TransactionSearch transactionObj);
//
//	Type202 ge202MsgDetails(TransactionSearch transactionObj);
//	
	String exportTransactionCSV(Transaction transactionObj) throws IOException;
//
//	ExportResponse exportTransactionExcel(Transaction transactionObj) throws IOException;
	
	
	DBResponse getUploadedMsgDataList(Transaction transactionObj);
	
//	
//	ExportResponse exportUploadedMsgDataExcel(Transaction transactionObj) throws IOException;
//
	String exportUploadedMsgDataCSV(Transaction transactionObj) throws IOException;
//	
//	ExportResponse exportTransactionPDF(Transaction transactionObj) throws IOException;


	DBResponse getReceivedMsgList(Transaction transactionObj);


	String exportReceivedTransactionCSV(Transaction transactionObj);


	ExportResponse exportReceivedTransactionExcel(Transaction transactionObj) throws IOException;


	DBResponse getReceivedMsgView(String msgType, String msgTypeKey);

	DBResponse getActivityDetail(Integer msgKey);
}
