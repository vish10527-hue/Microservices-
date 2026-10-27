package in.ameya.dao.reports;

import in.ameya.model.DBResponse;

public interface MsgReportDao {

    DBResponse getNostroSummaryList( String fromDate, String toDate, Integer loginUserKey);
    
}
