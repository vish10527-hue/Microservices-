package in.ameya.schedulers.service.Impl.fileSchedularServiceImpl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.schedulers.dao.fileSchedularDao.CredenceMirrorExcelDao;
import in.ameya.schedulers.service.fileSchedularService.CredenceMirrorExcelService;
import in.ameya.schedulers.vo.fileSeries.CredenceMirrorExcelVo;

@Service
@Transactional(transactionManager = "MainTransactionManager")
public class CredenceMirrorExcelServiceImpl implements CredenceMirrorExcelService {

    @Autowired
    private CredenceMirrorExcelDao credenceMirrorExcelDao;

    @Override
    public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag) {
        return credenceMirrorExcelDao.isFileUploaded(msgType, strFileName, sourceSystem, fileModDateTime, murNumber, inOutFlag);
    }

    @Override
    public String saveTransactionsInBatches( List<CredenceMirrorExcelVo> list, String fileName, Integer uploadKey) {
        return credenceMirrorExcelDao.saveTransactionsInBatches( list, fileName, uploadKey);
    }

    @Override
    public List<CredenceMirrorExcelVo> readExcel(File file) throws Exception {
        return credenceMirrorExcelDao.readExcel(file);
    }

    @Override
    public void validateUploadFile(Integer uploadKey) {
        credenceMirrorExcelDao.validateUploadFile(uploadKey);
    }
}
