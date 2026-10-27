package in.ameya.service.reports;

import in.ameya.model.DBResponse;

public interface MsgReportService {

    DBResponse getNostroSummaryList(String fromDate, String toDate, Integer userId);
}
