package in.ameya.schedulers.service.Impl.fileSchedularServiceImpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.schedulers.dao.fileSchedularDao.CredenceMirrorDao;
import in.ameya.schedulers.service.fileSchedularService.CredenceMirrorService;
import in.ameya.schedulers.vo.fileSeries.CredenceMirrorVo;
import in.ameya.schedulers.vo.fileSeries.FittCsvVo;
import in.ameya.schedulers.vo.fileSeries.OtherTxnsCsvVo;

@Service
@Transactional(transactionManager = "MainTransactionManager")
public class CredenceMirrorServiceImpl implements CredenceMirrorService  {

	@Autowired
    private CredenceMirrorDao credenceMirrorDao;
	
	@Override
    public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag) {
        return credenceMirrorDao.isFileUploaded(msgType,strFileName,sourceSystem,fileModDateTime,murNumber,1);
    }
	
	@Override
    public String saveTransactionsInBatches(List<CredenceMirrorVo> list,String fileName,Integer uploadKey) {
        return credenceMirrorDao.saveTransactionsInBatches(list, fileName, uploadKey);
    }

	@Override
	public List<CredenceMirrorVo> readFile(File file) throws Exception {
	    return credenceMirrorDao.readFile(file);
	}

	@Override
	public void validateUploadFile(Integer uploadKey) {
		credenceMirrorDao.validateUploadFile(uploadKey);
	}
}
