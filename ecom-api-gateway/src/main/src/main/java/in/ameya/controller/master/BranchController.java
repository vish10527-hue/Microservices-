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
import in.ameya.model.master.BranchDetails;
import in.ameya.model.master.BranchSearch;
import in.ameya.service.master.BranchService;

@RestController
@RequestMapping("/branch")
public class BranchController {
    
    private static final Logger logger = LogManager.getLogger(BranchController.class);
    
    @Autowired
    BranchService branchService;

    @PostMapping("/list")
    public ResponseEntity<BranchSearch> getBranchList(@RequestBody BranchSearch branchSearch, @RequestAttribute Integer userId) {
        try {
            branchSearch.setLoggedInKey(userId);
            DBResponse status = branchService.getBranchList(branchSearch);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(branchSearch, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("getBranchList() : Exception in BranchController-getBranchList", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("getBranchList() : Unexpected error in BranchController-getBranchList", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<DBResponse> addBranch(@RequestBody BranchDetails branch, @RequestAttribute Integer userId) {
        try {
            DBResponse response = branchService.addBranch(branch, userId);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("addBranch() : Error in BranchController-addBranch", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<DBResponse> updateBranch(@RequestBody BranchDetails branch, @RequestAttribute Integer userId) {
        try {
            DBResponse response = branchService.updateBranch(branch, userId);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("updateBranch() : Error in BranchController-updateBranch", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/discard")
    public ResponseEntity<DBResponse> discardBranch(@RequestBody BranchDetails branch, @RequestAttribute Integer userId) {
        try {
            DBResponse response = branchService.discardBranch(branch, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in BranchController-discardBranch", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<DBResponse> verifyBranchRecord(@RequestBody BranchSearch branchSearch, @RequestAttribute Integer userId) {
        try {
            DBResponse resp = branchService.verifyBranchRecord(branchSearch, userId);
            HttpStatusCode status = (resp.getStatus() == 1) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(resp, status);
        } catch (Exception e) {
            logger.error("verifyBranchRecord() : Error in BranchController-verifyBranchRecord", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/csv")
    public ResponseEntity<DBResponse> getCSV(@RequestBody BranchSearch branchSearch, @RequestAttribute Integer userId) {
        try {
            branchSearch.setLoggedInKey(userId);
            String result = branchService.exportBranchCSV(branchSearch);

            if (result != null && !result.isEmpty()) {
                String[] resultParts = result.split("\\|", 2);
                String filename = resultParts[0];
                String csvData = (resultParts.length > 1) ? resultParts[1] : "";
                return new ResponseEntity<>(new DBResponse(1, filename, csvData), HttpStatus.OK);
            } else {
                logger.warn("getCSV() : No data found in BranchController-getCSV");
                return new ResponseEntity<>(new DBResponse(-1, "No data found", null), HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            logger.error("Error in BranchController-getCSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/excel")
    public ResponseEntity<DBResponse> getExcel(@RequestBody BranchSearch branchSearch, @RequestAttribute Integer userId) {
        try {
            branchSearch.setLoggedInKey(userId);
            ExportResponse exportResponse = branchService.exportBranchExcel(branchSearch);

            DBResponse response = new DBResponse(
                (exportResponse.getBase64Data() != null) ? 1 : -1,
                exportResponse.getFilename(),
                exportResponse.getBase64Data()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("getExcel() : Error in BranchController-getExcel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view/{branchKey}")
    public ResponseEntity<BranchDetails> viewBranch(@PathVariable Integer branchKey, @RequestAttribute Integer userId) {
        try {
            BranchDetails branchDetails = branchService.getBranchDetailsById(branchKey, userId);
            if (branchDetails != null) {
                return ResponseEntity.ok(branchDetails);
            } else {
                logger.warn("Branch not found in BranchController-viewBranch");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (CommonDataException e) {
            logger.error("viewBranch() : CommonDataException in BranchController-viewBranch", e.getMessage(), e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("viewBranch() : Unexpected error in BranchController-viewBranch", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

