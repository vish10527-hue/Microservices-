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
import in.ameya.model.master.UserRoleDetails;
import in.ameya.model.master.UserRoleSearch;
import in.ameya.service.master.UserRoleService;

@RestController
@RequestMapping("/user-role")
public class UserRoleController {

    @Autowired
    UserRoleService userRoleService;
    private static final Logger logger = LogManager.getLogger(UserRoleController.class);


    @PostMapping("/list")
    public ResponseEntity<UserRoleSearch> getUserRoleList(@RequestBody UserRoleSearch userRoleSearch, @RequestAttribute Integer userId) {
        try {
            userRoleSearch.setLoggedInKey(userId);
            DBResponse status = userRoleService.getUserRoleList(userRoleSearch);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(userRoleSearch, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("UserRoleController - getUserRoleList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("UserRoleController - getUserRoleList: Error fetching user role list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<DBResponse> addUserRole(@RequestBody UserRoleDetails userRole, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(userRoleService.addUserRole(userRole, userId), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("UserRoleController - addUserRole: Error adding user role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<DBResponse> updateUserRole(@RequestBody UserRoleDetails userRole, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(userRoleService.updateUserRole(userRole, userId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("UserRoleController - updateUserRole: Error updating user role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/discard")
    public ResponseEntity<DBResponse> discardUserRole(@RequestBody UserRoleDetails userRole, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(userRoleService.discardUserRole(userRole, userId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("UserRoleController - discardUserRole: Error discarding user role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<DBResponse> verifyUserRoleRecord(@RequestBody UserRoleSearch userRoleSearch, @RequestAttribute Integer userId) {
        try {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DBResponse resp = userRoleService.verifyUserRoleRecord(userRoleSearch, userId);

            if (resp.getStatus() == 1) {
                status = HttpStatus.OK;
            }

            return new ResponseEntity<>(resp, status);
        } catch (Exception e) {
            logger.error("UserRoleController - verifyUserRoleRecord: Error verifying user role record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/csv")
    public ResponseEntity<DBResponse> getCSV(@RequestBody UserRoleSearch userRoleSearch, @RequestAttribute Integer userId) {
        try {
            userRoleSearch.setLoggedInKey(userId);
            String result = userRoleService.exportUserRoleCSV(userRoleSearch);

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
            logger.error("UserRoleController - getCSV: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("UserRoleController - getCSV: Error exporting user role list to CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/excel")
    public ResponseEntity<DBResponse> getExcel(@RequestBody UserRoleSearch userRoleSearch, @RequestAttribute Integer userId) {
        try {
            userRoleSearch.setLoggedInKey(userId);
            ExportResponse exportResponse = userRoleService.exportUserRoleExcel(userRoleSearch);

            DBResponse response = new DBResponse(
                exportResponse.getBase64Data() != null ? 1 : -1,
                exportResponse.getFilename(),
                exportResponse.getBase64Data()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("UserRoleController - getExcel: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("UserRoleController - getExcel: Error exporting user role list to Excel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view/{userRoleKey}")
    public ResponseEntity<UserRoleDetails> getUserRoleDetails(@PathVariable Integer userRoleKey, @RequestAttribute Integer userId) {
        try {
            UserRoleDetails userRoleDetails = userRoleService.getUserRoleDetailsById(userRoleKey, userId);

            if (userRoleDetails != null) {
                return ResponseEntity.ok(userRoleDetails);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (CommonDataException e) {
            logger.error("UserRoleController - getUserRoleDetails: CommonDataException occurred", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("UserRoleController - getUserRoleDetails: Error retrieving user role details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
