package in.ameya.schedulers.service.Impl.fileSchedularServiceImpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.schedulers.dao.fileSchedularDao.OtherTxnsCsvDao;
import in.ameya.schedulers.service.fileSchedularService.OtherTxnsCsvService;
import in.ameya.schedulers.vo.fileSeries.FottCsvVo;
import in.ameya.schedulers.vo.fileSeries.OtherTxnsCsvVo;

@Service
@Transactional(transactionManager = "MainTransactionManager")
public class OtherTxnsCsvServiceImpl implements OtherTxnsCsvService  {

	@Autowired
    private OtherTxnsCsvDao otherTxnsCsvDao;

	@Override
    public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag) {
        return otherTxnsCsvDao.isFileUploaded(msgType,strFileName,sourceSystem,fileModDateTime,murNumber,1);
    }
	
	@Override
    public String saveTransactionsInBatches(List<OtherTxnsCsvVo> list,String fileName,Integer uploadKey) {
        return otherTxnsCsvDao.saveTransactionsInBatches(list, fileName, uploadKey);
    }

	@Override
	public List<OtherTxnsCsvVo> readFile(File file) throws Exception {
	    return otherTxnsCsvDao.readFile(file);
	}

	@Override
	public void validateUploadFile(Integer uploadKey) {
		otherTxnsCsvDao.validateUploadFile(uploadKey);
	}

}
