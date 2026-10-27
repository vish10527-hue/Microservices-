package in.ameya.schedulers.service.Impl.fileSchedularServiceImpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.schedulers.dao.fileSchedularDao.FittCsvDao;
import in.ameya.schedulers.service.fileSchedularService.FittCsvService;
import in.ameya.schedulers.vo.fileSeries.FittCsvVo;

@Service
@Transactional(transactionManager = "MainTransactionManager")
public class FittCsvServiceImpl implements FittCsvService  {

	@Autowired
    private FittCsvDao fittCsvDao;

	@Override
    public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag) {
        return fittCsvDao.isFileUploaded(msgType,strFileName,sourceSystem,fileModDateTime,murNumber,1);
    }
	
	@Override
    public String saveTransactionsInBatches(List<FittCsvVo> list,String fileName,Integer uploadKey) {
        return fittCsvDao.saveTransactionsInBatches(list, fileName, uploadKey);
    }

	@Override
	public List<FittCsvVo> readFile(File file) throws Exception {
	    return fittCsvDao.readFile(file);
	}

	@Override
	public void validateUploadFile(Integer uploadKey) {
		fittCsvDao.validateUploadFile(uploadKey);
	}

}
