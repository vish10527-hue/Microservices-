package in.ameya.controller.transaction;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.transaction.Transaction;
import in.ameya.schedulers.vo.fileSeries.ActivityMsgVo;
import in.ameya.service.transaction.TransactionService;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	   
    @Autowired
    TransactionService transactionService;
    
	@PostMapping("/getUploadMsgList")
    public ResponseEntity<Transaction> getUploadedMsgDataList(@RequestBody Transaction transactionObj, @RequestAttribute Integer userId) {
        try {
        	transactionObj.setLoggedInKey(String.valueOf(userId));
            DBResponse status = transactionService.getUploadedMsgDataList(transactionObj);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(transactionObj, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("TransactionController - getTransactionList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("TransactionController - getTransactionList: Error fetching transaction list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@PostMapping("/export/UploadMsg/csv")
    public ResponseEntity<DBResponse> exportUploadedMsgDataCSV(@RequestBody Transaction transactionObj, @RequestAttribute String userId) {
        try {
        	transactionObj.setUserId(userId);
            String result = transactionService.exportUploadedMsgDataCSV(transactionObj);
            if (result != null && !result.isEmpty()) {
                String[] resultParts = result.split("\\|", 2);
                String filename = resultParts[0];
                String csvData = resultParts.length > 1 ? resultParts[1] : "";

                DBResponse response = new DBResponse(1, filename, csvData);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new DBResponse(-1, "No data found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            logger.error("TransactionController - exportSearchedTransactionCSV: Error exporting search result to CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@PostMapping("/export/csv")
    public ResponseEntity<DBResponse> getCSV(@RequestBody Transaction transactionObj, @RequestAttribute Integer userId) {
        try {
        	transactionObj.setLoggedInKey(String.valueOf(userId));
            String result = transactionService.exportTransactionCSV(transactionObj);

            if (result != null && !result.isEmpty()) {
                String[] resultParts = result.split("\\|", 2);
                String filename = resultParts[0];
                String csvData = (resultParts.length > 1) ? resultParts[1] : "";
                return new ResponseEntity<>(new DBResponse(1, filename, csvData), HttpStatus.OK);
            } else {
                logger.warn("getCSV() : No data found in TransactionController-getCSV");
                return new ResponseEntity<>(new DBResponse(-1, "No data found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            logger.error("Error in TransactionController-getCSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@PostMapping("/getReceivedMsgList")
    public ResponseEntity<Transaction> getReceivedMsgList(@RequestBody Transaction transactionObj, @RequestAttribute Integer userId) {
        try {
        	transactionObj.setLoggedInKey(String.valueOf(userId));
            DBResponse status = transactionService.getReceivedMsgList(transactionObj);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(transactionObj, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("TransactionController - getTransactionList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("TransactionController - getTransactionList: Error fetching transaction list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@GetMapping("/getReceivedMsgView")
    public ResponseEntity<Object> getReceivedMsgDataView(@RequestParam String msgType, @RequestParam String msgTypeKey) {
        try {
        	DBResponse response = transactionService.getReceivedMsgView(msgType, msgTypeKey);

        	if (response.getStatus() == 1) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (response.getStatus() == 0) {
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            } else {
                throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, response);
            }
        } catch (CommonDataException e) {
            logger.error("TransactionController - getReceivedMsgView: Error occurred", e);
            return ResponseEntity.status(e.status())
                .body(new DBResponse(-1, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("TransactionController - getReceivedMsgView: Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DBResponse(-1, "Internal server error", null));
        }
    }
	
	@PostMapping("/received/export/csv")
    public ResponseEntity<DBResponse> getReceivedCSV(@RequestBody Transaction transactionObj, @RequestAttribute Integer userId) {
        try {
        	transactionObj.setLoggedInKey(String.valueOf(userId));
            String result = transactionService.exportReceivedTransactionCSV(transactionObj);

            if (result != null && !result.isEmpty()) {
                String[] resultParts = result.split("\\|", 2);
                String filename = resultParts[0];
                String csvData = (resultParts.length > 1) ? resultParts[1] : "";
                return new ResponseEntity<>(new DBResponse(1, filename, csvData), HttpStatus.OK);
            } else {
                logger.warn("getCSV() : No data found in TransactionController-getCSV");
                return new ResponseEntity<>(new DBResponse(-1, "No data found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            logger.error("Error in TransactionController-getCSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@PostMapping("/received/export/excel")
    public ResponseEntity<DBResponse> getReceivedMsgExcel(@RequestBody Transaction transactionObj, @RequestAttribute Integer userId) {
        try {
        	transactionObj.setLoggedInKey(String.valueOf(userId));
        	ExportResponse exportResponse = transactionService.exportReceivedTransactionExcel(transactionObj);

            DBResponse response = new DBResponse(
                    (exportResponse.getBase64Data() != null) ? 1 : -1,
                    exportResponse.getFilename(),
                    exportResponse.getBase64Data()
                );
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in TransactionController-getCSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@GetMapping("/getActivityDetailList")
    public ResponseEntity<DBResponse> getActivityDetailList(@RequestParam Integer msgKey) {
        try {
            DBResponse response = transactionService.getActivityDetail(msgKey);

            if (response.getStatus() == 1) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (response.getStatus() == 0) {
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            } else {
                throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, response);
            }
        } catch (CommonDataException e) {
            logger.error("TransactionController - getTransactionList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("TransactionController - getTransactionList: Error fetching transaction list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
