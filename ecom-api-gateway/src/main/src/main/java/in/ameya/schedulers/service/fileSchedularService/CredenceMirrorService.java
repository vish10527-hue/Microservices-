package in.ameya.schedulers.service.fileSchedularService;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.CredenceMirrorVo;
import in.ameya.schedulers.vo.fileSeries.FittCsvVo;
import in.ameya.schedulers.vo.fileSeries.OtherTxnsCsvVo;

public interface CredenceMirrorService {

	public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

	public String saveTransactionsInBatches(List<CredenceMirrorVo> transactionList,String fileName, Integer uploadKey);

	public List<CredenceMirrorVo> readFile(File file) throws Exception ;

	public void validateUploadFile(Integer uploadKey);
 
}
