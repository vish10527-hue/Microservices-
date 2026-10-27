package in.ameya.controller.master;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.NostroAcctsDetails;
import in.ameya.model.master.NostroAcctsSearch;
import in.ameya.service.master.NostroAcctsService;



@RestController
@RequestMapping("/nostroAccts")
public class NostroAcctsController {
    
    private static final Logger logger = LogManager.getLogger(NostroAcctsController.class);
    
    @Autowired
    NostroAcctsService nostroAcctsService;

    @PostMapping("/list")
    public ResponseEntity<NostroAcctsSearch> getNostroAcctsList(@RequestBody NostroAcctsSearch nostroAcctsSearch, @RequestAttribute Integer userId) {
        try {
            nostroAcctsSearch.setLoggedInKey(userId);
            DBResponse status = nostroAcctsService.getNostroAcctsList(nostroAcctsSearch);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(nostroAcctsSearch, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("getNostroAcctsList() : Exception in NostroAcctsController-getNostroAcctsList", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("getNostroAcctsList() : Unexpected error in NostroAcctsController-getNostroAcctsList", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<DBResponse> addNostroAccts(@RequestBody NostroAcctsDetails nostroAccts, @RequestAttribute Integer userId) {
        try {
            DBResponse response = nostroAcctsService.addNostroAccts(nostroAccts, userId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("addNostroAccts() : Error in NostroAcctsController-addNostroAccts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<DBResponse> updateNostroAccts(@RequestBody NostroAcctsDetails nostroAccts, @RequestAttribute Integer userId) {
        try {
            DBResponse response = nostroAcctsService.updateNostroAccts(nostroAccts, userId);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("updateNostroAccts() : Error in NostroAcctsController-updateNostroAccts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/discard")
    public ResponseEntity<DBResponse> discardNostroAccts(@RequestBody NostroAcctsDetails nostroAccts, @RequestAttribute Integer userId) {
        try {
            DBResponse response = nostroAcctsService.discardNostroAccts(nostroAccts, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in NostroAcctsController-discardNostroAccts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<DBResponse> verifyNostroAcctsRecord(@RequestBody NostroAcctsSearch nostroAcctsSearch, @RequestAttribute Integer userId) {
        try {
            DBResponse resp = nostroAcctsService.verifyNostroAcctsRecord(nostroAcctsSearch, userId);
            HttpStatusCode status = (resp.getStatus() == 1) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(resp, status);
        } catch (Exception e) {
            logger.error("verifyNostroAcctsRecord() : Error in NostroAcctsController-verifyNostroAcctsRecord", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/csv")
    public ResponseEntity<DBResponse> getCSV(@RequestBody NostroAcctsSearch nostroAcctsSearch, @RequestAttribute Integer userId) {
        try {
            nostroAcctsSearch.setLoggedInKey(userId);
            String result = nostroAcctsService.exportNostroAcctsCSV(nostroAcctsSearch);

            if (result != null && !result.isEmpty()) {
                String[] resultParts = result.split("\\|", 2);
                String filename = resultParts[0];
                String csvData = (resultParts.length > 1) ? resultParts[1] : "";
                return new ResponseEntity<>(new DBResponse(1, filename, csvData), HttpStatus.OK);
            } else {
                logger.warn("getCSV() : No data found in NostroAcctsController-getCSV");
                return new ResponseEntity<>(new DBResponse(-1, "No data found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            logger.error("Error in NostroAcctsController-getCSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/excel")
    public ResponseEntity<DBResponse> getExcel(@RequestBody NostroAcctsSearch nostroAcctsSearch, @RequestAttribute Integer userId) {
        try {
            nostroAcctsSearch.setLoggedInKey(userId);
            ExportResponse exportResponse = nostroAcctsService.exportNostroAcctsExcel(nostroAcctsSearch);

            DBResponse response = new DBResponse(
                (exportResponse.getBase64Data() != null) ? 1 : -1,
                exportResponse.getFilename(),
                exportResponse.getBase64Data()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getExcel() : Error in NostroAcctsController-getExcel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view/{nostroAcctsKey}")
    public ResponseEntity<NostroAcctsDetails> viewNostroAccts(@PathVariable Integer nostroAcctsKey, @RequestAttribute Integer userId) {
        try {
            NostroAcctsDetails nostroAcctsDetails = nostroAcctsService.getNostroAcctsDetailsById(nostroAcctsKey, userId);
            if (nostroAcctsDetails != null) {
                return ResponseEntity.ok(nostroAcctsDetails);
            } else {
                logger.warn("NostroAccts not found in NostroAcctsController-viewNostroAccts");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (CommonDataException e) {
            logger.error("viewNostroAccts() : CommonDataException in NostroAcctsController-viewNostroAccts", e.getMessage(), e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("viewNostroAccts() : Unexpected error in NostroAcctsController-viewNostroAccts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
