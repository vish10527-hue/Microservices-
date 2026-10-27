package in.ameya.controller.master;
import java.io.IOException;

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
import in.ameya.model.ExportResponse ;
import in.ameya.model.master.LookupDetails;
import in.ameya.model.master.LookupSearch;
import in.ameya.service.master.LookupService;

@RestController
@RequestMapping("/lookup")
public class LookupController {

    @Autowired
    LookupService lookupService;

    private static final Logger logger = LogManager.getLogger(LookupController.class);

    @PostMapping("/list")
    public ResponseEntity<LookupSearch> getLookupList(@RequestBody LookupSearch lookupSearch, @RequestAttribute Integer userId) {
        try {
            lookupSearch.setLoggedInKey(userId);
            DBResponse status = lookupService.getLookupList(lookupSearch);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(lookupSearch, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("LookupController - getLookupList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("LookupController - getLookupList: Error fetching lookup list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<DBResponse> addLookup(@RequestBody LookupDetails lookup, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(lookupService.addLookup(lookup, userId), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("LookupController - addLookup: Error adding lookup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<DBResponse> updateLookup(@RequestBody LookupDetails lookup, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(lookupService.updateLookup(lookup, userId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("LookupController - updateLookup: Error updating lookup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/discard")
    public ResponseEntity<DBResponse> discardLookup(@RequestBody LookupDetails lookup, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(lookupService.discardLookup(lookup, userId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("LookupController - discardLookup: Error discarding lookup", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<DBResponse> verifyLookupRecord(@RequestBody LookupSearch lookupSearch, @RequestAttribute Integer userId) {
        try {
        	HttpStatusCode status = HttpStatus.BAD_REQUEST;
    		DBResponse resp = lookupService.verifyLookupRecord(lookupSearch, userId);
    		if(resp.getStatus() == 1) {
    			status = HttpStatus.OK;
    		} 
            return new ResponseEntity<>(resp, status);
        } catch (Exception e) {
            logger.error("LookupController - verifyLookupRecord: Error verifying lookup record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/csv")
    public ResponseEntity<DBResponse> getCSV(@RequestBody LookupSearch lookupSearch, @RequestAttribute Integer userId) {
        try {
            lookupSearch.setLoggedInKey(userId);
            String result = lookupService.exportLookupCSV(lookupSearch);

            if (result != null && !result.isEmpty()) {
                String[] resultParts = result.split("\\|", 2);
                String filename = resultParts[0];
                String csvData = resultParts.length > 1 ? resultParts[1] : "";

                DBResponse response = new DBResponse(1, filename, csvData);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new DBResponse(-1, "No data found", null), HttpStatus.NO_CONTENT);
            }
        } catch (IOException e) {
            logger.error("LookupController - getCSV: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("LookupController - getCSV: Error exporting lookup list to CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/excel")
    public ResponseEntity<DBResponse> getExcel(@RequestBody LookupSearch lookupSearch, @RequestAttribute Integer userId) {
        try {
            lookupSearch.setLoggedInKey(userId);
            ExportResponse exportResponse = lookupService.exportLookupExcel(lookupSearch);

            DBResponse response = new DBResponse(
                exportResponse.getBase64Data() != null ? 1 : -1,
                exportResponse.getFilename(),
                exportResponse.getBase64Data()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("LookupController - getExcel: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("LookupController - getExcel: Error exporting lookup list to Excel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view/{lookupId}")
    public ResponseEntity<LookupDetails> viewLookup(@PathVariable Integer lookupId, @RequestAttribute Integer userId) {
        try {
            LookupDetails lookupDetails = lookupService.getLookupDetailsById(lookupId, userId);

            if (lookupDetails != null) {
                return ResponseEntity.ok(lookupDetails);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (CommonDataException e) {
            logger.error("LookupController - viewLookup: CommonDataException occurred", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("LookupController - viewLookup: Error retrieving lookup details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
