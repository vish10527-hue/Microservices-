package in.ameya.schedulers.dao.fileSchedularDao;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.CredenceMirrorExcelVo;

public interface CredenceMirrorExcelDao {

    Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

    String saveTransactionsInBatches(List<CredenceMirrorExcelVo> transactionList, String fileName, Integer uploadKey);

    List<CredenceMirrorExcelVo> readExcel(File file) throws Exception;

    void validateUploadFile(Integer uploadKey);
}
