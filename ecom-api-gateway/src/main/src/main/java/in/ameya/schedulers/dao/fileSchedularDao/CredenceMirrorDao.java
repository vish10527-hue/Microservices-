package in.ameya.schedulers.dao.fileSchedularDao;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.CredenceMirrorVo;

public interface CredenceMirrorDao {

	Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

	String saveTransactionsInBatches(List<CredenceMirrorVo> transactionList, String fileName,Integer uploadKey);

	List<CredenceMirrorVo> readFile(File file) throws Exception;

	void validateUploadFile(Integer uploadKey);
    
}
