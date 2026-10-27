package in.ameya.schedulers.dao.fileSchedularDao;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.FittCsvVo;

public interface FittCsvDao {

	Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

	String saveTransactionsInBatches(List<FittCsvVo> transactionList, String fileName,Integer uploadKey);

	List<FittCsvVo> readFile(File file) throws Exception;

	void validateUploadFile(Integer uploadKey);
    
}
