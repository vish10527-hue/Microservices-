package in.ameya.service.impl.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ameya.dao.reports.MsgReportDao;
import in.ameya.model.DBResponse;
import in.ameya.service.reports.MsgReportService;

@Service
public class MsgReportServiceImpl implements MsgReportService {

    @Autowired
    private MsgReportDao msgReportDao;

    @Override
    public DBResponse getNostroSummaryList(String fromDate, String toDate, Integer userId) {

        return msgReportDao.getNostroSummaryList(fromDate, toDate, userId);
    }
}
