package in.ameya.schedulers.dao.fileSchedularDao;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.FottCsvVo;


public interface FottCsvDao {

	Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

	String saveTransactionsInBatches(List<FottCsvVo> transactionList, String fileName,Integer uploadKey);

	List<FottCsvVo> readFile(File file) throws Exception;

	void validateUploadFile(Integer uploadKey);
    
}
