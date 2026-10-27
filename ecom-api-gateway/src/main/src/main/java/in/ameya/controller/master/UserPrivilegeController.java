package in.ameya.controller.master;

import java.util.List;

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
import in.ameya.model.master.UserPrivilegeDetails;
import in.ameya.model.master.UserPrivilegeSearch;
import in.ameya.service.master.UserPrivilegeService;

@RestController
@RequestMapping("/user-role-privilege")
public class UserPrivilegeController {

	@Autowired
	UserPrivilegeService userPrivilegeService;

	private static final Logger logger = LogManager.getLogger(UserPrivilegeController.class);

	@PostMapping("/list")
	public ResponseEntity<UserPrivilegeSearch> getUserPrivilegeList(@RequestBody() UserPrivilegeSearch userPrivilegeSearch, @RequestAttribute() Integer userId) {
	    try {
	        userPrivilegeSearch.setLoggedInKey(userId);
	        DBResponse status = userPrivilegeService.getUserPrivilegeList(userPrivilegeSearch);
	        if (status.getStatus() == 1) {
	            return new ResponseEntity<>(userPrivilegeSearch, HttpStatus.OK);
	        } else {
	            throw new CommonDataException(HttpStatus.NO_CONTENT, status);
	        }
	    } catch (CommonDataException e) {
	        logger.error("getUserPrivilegeList() : CommonDataException", e);
	        throw e;
	    } catch (Exception e) {
	        logger.error("getUserPrivilegeList() : Exception", e);
	        throw new RuntimeException(e);
	    }
	}

	@PostMapping("/assign")
	public ResponseEntity<DBResponse> addUserPrivilege(@RequestBody UserPrivilegeDetails userPrivilege, @RequestAttribute Integer userId) {
	    try {
	    	return new ResponseEntity<>(userPrivilegeService.addUserPrivilege(userPrivilege, userId), HttpStatus.CREATED);
	    } catch (Exception e) {
	    	logger.error("addUserPrivilege() : Error adding userPrivilege", e);
	        throw new RuntimeException(e);
	    }
	}
	
	@PutMapping("/update")
	public ResponseEntity<DBResponse> updateUserPrivilege(@RequestBody UserPrivilegeDetails userPrivilege, @RequestAttribute() Integer userId) {
	    try {
	    	return new ResponseEntity<>(userPrivilegeService.updateUserPrivilege(userPrivilege, userId), HttpStatus.ACCEPTED);
	    } catch (Exception e) {
	        logger.error("updateUserPrivilege() : Exception", e);
	        throw new RuntimeException(e);
	    }
	}

	@DeleteMapping("/discard")
	public ResponseEntity<DBResponse> discardUserPrivilege(@RequestBody UserPrivilegeDetails userPrivilege, @RequestAttribute() Integer userId) {
	    try {
	    	return new ResponseEntity<>(userPrivilegeService.discardUserPrivilege(userPrivilege, userId), HttpStatus.CREATED);
	    } catch (Exception e) {
	        logger.error("discardUserPrivilege() : Exception", e);
	        throw new RuntimeException(e);
	    }
	}

	@PostMapping("/verify")
	public ResponseEntity<DBResponse> verifyUserPrivilegeRecord(@RequestBody UserPrivilegeSearch userPrivilegeSearch, @RequestAttribute() Integer userId) {
	    try {
	        DBResponse resp = userPrivilegeService.verifyUserPrivilegeRecord(userPrivilegeSearch, userId);
	        HttpStatusCode status = (resp.getStatus() == 1) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
	        return new ResponseEntity<>(resp, status);
	    } catch (Exception e) {
	        logger.error("verifyUserPrivilegeRecord() : Exception", e);
	        throw new RuntimeException(e);
	    }
	}

	@GetMapping("/view/{userRoleKey}/{approvedFlg}")
	public ResponseEntity<List<UserPrivilegeDetails>> getUserPrivilegeDetails(@PathVariable String userRoleKey, @PathVariable Integer approvedFlg, @RequestAttribute Integer userId) {
	    try {
	        List<UserPrivilegeDetails> userPrivilegeDetails = userPrivilegeService.getUserPrivilegeDetailsById(userRoleKey, approvedFlg, userId);
	        return ResponseEntity.ok(userPrivilegeDetails);
	    } catch (CommonDataException e) {
	        logger.error("getUserPrivilegeDetails() : CommonDataException", e);
	        return ResponseEntity.status(e.status()).body(null);
	    } catch (Exception e) {
	        logger.error("getUserPrivilegeDetails() : Exception", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	@PostMapping("/attach")
	public ResponseEntity<List<UserPrivilegeDetails>> attachUserPrivilege(@RequestBody UserPrivilegeSearch userPrivilegeSearch, @RequestAttribute Integer userId) {
	    try {
	        List<UserPrivilegeDetails> userPrivilegeDetails = userPrivilegeService.attachUserPrivilege(userPrivilegeSearch, userId);
	        return ResponseEntity.ok(userPrivilegeDetails);
	    } catch (CommonDataException e) {
	        logger.error("attachUserPrivilege() : CommonDataException", e);
	        return ResponseEntity.status(e.status()).body(null);
	    } catch (Exception e) {
	        logger.error("attachUserPrivilege() : Exception", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
}
