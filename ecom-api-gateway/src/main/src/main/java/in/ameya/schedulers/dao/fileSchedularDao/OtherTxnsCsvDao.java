package in.ameya.schedulers.dao.fileSchedularDao;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.FottCsvVo;
import in.ameya.schedulers.vo.fileSeries.OtherTxnsCsvVo;


public interface OtherTxnsCsvDao {

	Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

	String saveTransactionsInBatches(List<OtherTxnsCsvVo> transactionList, String fileName,Integer uploadKey);

	List<OtherTxnsCsvVo> readFile(File file) throws Exception;

	void validateUploadFile(Integer uploadKey);
    
}
