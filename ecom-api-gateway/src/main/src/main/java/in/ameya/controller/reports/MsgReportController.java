package in.ameya.controller.reports;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ameya.controller.master.BranchController;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.service.reports.MsgReportService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/msgReport")
@CrossOrigin("*")
public class MsgReportController {

    private static final Logger logger = LogManager.getLogger(BranchController.class);

	
    @Autowired
    private MsgReportService msgReportService;

    @PostMapping("/getNostroSummaryList")
    public ResponseEntity<Object> getNostroSummaryList(@RequestBody Map<String, String> request) {
// @RequestAttribute Integer userId
    	Integer userId=2;
        try {
            String fromDate = request.get("fromDate");
            String toDate = request.get("toDate");

            DBResponse status =msgReportService.getNostroSummaryList(fromDate, toDate, userId);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(status, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }

        } catch (CommonDataException e) {
            logger.error( "getNostroSummaryList - getNostroSummaryList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);

        } catch (Exception e) { 
        	logger.error("getNostroSummaryList - getNostroSummaryList: Error fetching nostro summary list", e );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
