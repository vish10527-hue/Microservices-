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
import in.ameya.model.master.UserDetails;
import in.ameya.model.master.UserSearch;
import in.ameya.service.master.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/list")
    public ResponseEntity<UserSearch> getUserList(@RequestBody UserSearch userSearch, @RequestAttribute Integer userId) {
        try {
            userSearch.setLoggedInKey(userId);
            DBResponse status = userService.getUserList(userSearch);

            if (status.getStatus() == 1) {
                return new ResponseEntity<>(userSearch, HttpStatus.OK);
            } else {
                throw new CommonDataException(HttpStatus.NO_CONTENT, status);
            }
        } catch (CommonDataException e) {
            logger.error("UserController - getUserList: No content found", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("UserController - getUserList: Error fetching user list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<DBResponse> addUser(@RequestBody UserDetails user, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(userService.addUser(user, userId), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("UserController - addUser: Error adding user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<DBResponse> updateUser(@RequestBody UserDetails user, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(userService.updateUser(user, userId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error("UserController - updateUser: Error updating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/discard")
    public ResponseEntity<DBResponse> discardUser(@RequestBody UserDetails user, @RequestAttribute Integer userId) {
        try {
            return new ResponseEntity<>(userService.discardUser(user, userId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("UserController - discardUser: Error discarding user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<DBResponse> verifyUserRecord(@RequestBody UserSearch userSearch, @RequestAttribute Integer userId) {
        try {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            DBResponse resp = userService.verifyUserRecord(userSearch, userId);

            if (resp.getStatus() == 1) {
                status = HttpStatus.OK;
            }

            return new ResponseEntity<>(resp, status);
        } catch (Exception e) {
            logger.error("UserController - verifyUserRecord: Error verifying user record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/csv")
    public ResponseEntity<DBResponse> getCSV(@RequestBody UserSearch userSearch, @RequestAttribute Integer userId) {
        try {
            userSearch.setLoggedInKey(userId);
            String result = userService.exportUserCSV(userSearch);

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
            logger.error("UserController - getCSV: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("UserController - getCSV: Error exporting user list to CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/export/excel")
    public ResponseEntity<DBResponse> getExcel(@RequestBody UserSearch userSearch, @RequestAttribute Integer userId) {
        try {
            userSearch.setLoggedInKey(userId);
            ExportResponse exportResponse = userService.exportUserExcel(userSearch);

            DBResponse response = new DBResponse(
                exportResponse.getBase64Data() != null ? 1 : -1,
                exportResponse.getFilename(),
                exportResponse.getBase64Data()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("UserController - getExcel: IO Exception occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("UserController - getExcel: Error exporting user list to Excel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view/{userKey}")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable Integer userKey, @RequestAttribute Integer userId) {
        try {
            UserDetails userDetails = userService.getUserDetailsById(userKey, userId);

            if (userDetails != null) {
                return ResponseEntity.ok(userDetails);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (CommonDataException e) {
            logger.error("UserController - getUserDetails: CommonDataException occurred", e);
            return ResponseEntity.status(e.status()).body(null);
        } catch (Exception e) {
            logger.error("UserController - getUserDetails: Error retrieving user details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
