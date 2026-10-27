package in.ameya.controller.master;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import in.ameya.model.master.CurrencyDetails;
import in.ameya.model.master.CurrencySearch;
import in.ameya.service.master.CurrencyService;
@RestController
@RequestMapping("/currency")
public class CurrencyController {

	private static final Logger logger = LogManager.getLogger(CurrencyController.class);

    @Autowired
    CurrencyService currencyService;

    @PostMapping("/list")
    public ResponseEntity<CurrencySearch> getCurrencyList(@RequestBody CurrencySearch currencySearch, @RequestAttribute Integer userId) {
        try {
            currencySearch.setLoggedInKey(userId);
            DBResponse status = currencyService.getCurrencyList(currencySearch);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(currencySearch, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("CurrencyController - getCurrencyList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("CurrencyController - getCurrencyList: Error fetching currency list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<DBResponse> addCurrency(@RequestBody CurrencyDetails currency, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(currencyService.addCurrency(currency, userId), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("CurrencyController - addCurrency: Error adding currency", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<DBResponse> updateCurrency(@RequestBody CurrencyDetails currency, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(currencyService.updateCurrency(currency, userId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("CurrencyController - updateCurrency: Error updating currency", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/discard")
    public ResponseEntity<DBResponse> discardCurrency(@RequestBody CurrencyDetails currency, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(currencyService.discardCurrency(currency, userId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("CurrencyController - discardCurrency: Error discarding currency", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<DBResponse> verifyCurrencyRecord(@RequestBody CurrencySearch currencySearch, @RequestAttribute Integer userId) {
        try {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DBResponse resp = currencyService.verifyCurrencyRecord(currencySearch, userId);
            if (resp.getStatus() == 1) {
                status = HttpStatus.OK;
            }
            return new ResponseEntity<>(resp, status);
        } catch (Exception e) {
            logger.error("CurrencyController - verifyCurrencyRecord: Error verifying currency record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/csv")
    public ResponseEntity<DBResponse> getCSV(@RequestBody CurrencySearch currencySearch, @RequestAttribute Integer userId) {
        try {
            currencySearch.setLoggedInKey(userId);
            String result = currencyService.exportCurrencyCSV(currencySearch);

            if (result != null && !result.isEmpty()) {
                String[] resultParts = result.split("\\|", 2);
                String filename = resultParts[0];
                String csvData = resultParts.length > 1 ? resultParts[1] : "";

                DBResponse responseData = new DBResponse(1, filename, csvData);
                return new ResponseEntity<>(responseData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new DBResponse(-1, "No data found", null), HttpStatus.NO_CONTENT);
            }
        } catch (IOException e) {
            logger.error("CurrencyController - getCSV: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("CurrencyController - getCSV: Error exporting currency list to CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/excel")
    public ResponseEntity<DBResponse> getExcel(@RequestBody CurrencySearch currencySearch, @RequestAttribute Integer userId) {
        try {
            currencySearch.setLoggedInKey(userId);
            ExportResponse exportResponse = currencyService.exportCurrencyExcel(currencySearch);
            DBResponse responseData = new DBResponse(
                exportResponse.getBase64Data() != null ? 1 : -1,
                exportResponse.getFilename(),
                exportResponse.getBase64Data()
            );
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("CurrencyController - getExcel: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("CurrencyController - getExcel: Error exporting currency list to Excel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view/{currencyId}")
    public ResponseEntity<CurrencyDetails> viewCurrency(@PathVariable Integer currencyId, @RequestAttribute Integer userId) {
        try {
            CurrencyDetails currencyDetails = currencyService.getCurrencyDetailsById(currencyId, userId);
            if (currencyDetails != null) {
                return ResponseEntity.ok(currencyDetails);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (CommonDataException e) {
            logger.error("CurrencyController - viewCurrency: CommonDataException occurred", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("CurrencyController - viewCurrency: Error retrieving currency details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

