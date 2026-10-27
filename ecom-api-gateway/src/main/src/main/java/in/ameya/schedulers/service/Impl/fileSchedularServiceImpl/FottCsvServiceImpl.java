package in.ameya.schedulers.service.Impl.fileSchedularServiceImpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.schedulers.dao.fileSchedularDao.FottCsvDao;
import in.ameya.schedulers.service.fileSchedularService.FottCsvService;
import in.ameya.schedulers.vo.fileSeries.FottCsvVo;

@Service
@Transactional(transactionManager = "MainTransactionManager")
public class FottCsvServiceImpl implements FottCsvService  {

	@Autowired
    private FottCsvDao fottCsvDao;

	@Override
    public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag) {
        return fottCsvDao.isFileUploaded(msgType,strFileName,sourceSystem,fileModDateTime,murNumber,1);
    }
	
	@Override
    public String saveTransactionsInBatches(List<FottCsvVo> list,String fileName,Integer uploadKey) {
        return fottCsvDao.saveTransactionsInBatches(list, fileName, uploadKey);
    }

	@Override
	public List<FottCsvVo> readFile(File file) throws Exception {
	    return fottCsvDao.readFile(file);
	}

	@Override
	public void validateUploadFile(Integer uploadKey) {
		fottCsvDao.validateUploadFile(uploadKey);
	}

}
