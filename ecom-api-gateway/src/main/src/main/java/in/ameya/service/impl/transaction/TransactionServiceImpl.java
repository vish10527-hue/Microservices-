package in.ameya.service.impl.transaction;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.transaction.TransactionDao;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.transaction.Transaction;
import in.ameya.service.transaction.TransactionService;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor = RuntimeException.class)
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionDao transactionDao;
//
//	@Override
//	public MessageDashboard getMessageDashboardCount(MessageDashboard messageDashboardObj) {
//		return transactionDao.getMessageDashboardCount(messageDashboardObj);
//	}
//
//	@Override
//	public Transaction searchTransactions(Transaction transactionObj) {
//		return transactionDao.searchTransactions(transactionObj);
//	}
//
//	@Override
//	public TransactionSearch geMessageTypeDetails(TransactionSearch transactionObj) {
//		return transactionDao.geMessageTypeDetails(transactionObj);
//	}
//
//	@Override
//	public Type103 ge103MsgDetails(TransactionSearch transactionObj) {
//		return transactionDao.ge103MsgDetails(transactionObj);
//	}
//
//	@Override
//	public Type202 ge202MsgDetails(TransactionSearch transactionObj) {
//		return transactionDao.ge202MsgDetails(transactionObj);
//	}
//
	@Override
	public String exportTransactionCSV(Transaction transactionObj) throws IOException {
		return transactionDao.exportTransactionCSV(transactionObj);
	}
//
//	@Override
//	public ExportResponse exportTransactionExcel(Transaction transactionObj) throws IOException {
//		return transactionDao.exportTransactionExcel(transactionObj);
//	}

	
	
	@Override
	public DBResponse getUploadedMsgDataList(Transaction transactionObj) {
		return transactionDao.getUploadedMsgDataList(transactionObj);
	}

	@Override
	public DBResponse getReceivedMsgView(String msgType, String msgTypeKey) {
		return transactionDao.getReceivedMsgView(msgType,msgTypeKey);
	}
//
//	@Override
//	public ExportResponse exportUploadedMsgDataExcel(Transaction transactionObj) throws IOException {
//		return transactionDao.exportUploadedMsgDataExcel(transactionObj);
//	}
//
	@Override
	public String exportUploadedMsgDataCSV(Transaction transactionObj) throws IOException {
		return transactionDao.exportUploadedMsgDataCSV(transactionObj);
	}
	
	@Override
	public DBResponse getReceivedMsgList(Transaction transactionObj) {
		return transactionDao.getReceivedMsgList(transactionObj);
	}
//	
//	@Override
//	public ExportResponse exportTransactionPDF(Transaction transactionObj) throws IOException {
//		return transactionDao.exportTransactionPDF(transactionObj);
//	}



	@Override
	public String exportReceivedTransactionCSV(Transaction transactionObj) {
		return transactionDao.exportReceivedTransactionCSV(transactionObj);
	}

	@Override
	public ExportResponse exportReceivedTransactionExcel(Transaction transactionObj) throws IOException {
		return transactionDao.exportReceivedTransactionExcel(transactionObj);
	}



	@Override
	public DBResponse getActivityDetail(Integer msgKey) {
		return transactionDao.getActivityDetail(msgKey);
	}
}