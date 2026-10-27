package in.ameya.controller.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.ameya.service.master.DropdownService;

@RestController
@RequestMapping("/dropdown")
public class DropdownController {

    @Autowired
    DropdownService dropdownService;

    @GetMapping(value = "/branch")
    @ResponseBody
    public ResponseEntity<?> getBranchList(@RequestParam(required = false) String branchDesc, @RequestParam(required = false) String branchCode) {
        return new ResponseEntity<>(dropdownService.getBranchList(branchDesc, branchCode), HttpStatus.OK);
    }

    @GetMapping(value = "/lookup")
    @ResponseBody
    public ResponseEntity<?> getLookup(@RequestParam(value = "lookupType") Integer lookupType, @RequestParam(required = false) String lookupCode, @RequestParam(required = false) String lookupDesc) {
        return new ResponseEntity<>(dropdownService.getLookup(lookupType, lookupCode, lookupDesc), HttpStatus.OK);
    }

    @GetMapping(value = "/user-role")
    @ResponseBody
    public ResponseEntity<?> getUserRole(@RequestParam(required = false) String userRoleCode, @RequestParam(required = false) String userRoleName) {
        return new ResponseEntity<>(dropdownService.getUserRole(userRoleCode, userRoleName), HttpStatus.OK);
    }

    @GetMapping(value = "/currency")
    @ResponseBody
    public ResponseEntity<?> getCurrency(@RequestParam(required = false) String currCode, @RequestParam(required = false) String currDesc) {
        return new ResponseEntity<>(dropdownService.getCurrency(currCode, currDesc), HttpStatus.OK);
    }
    
    @GetMapping(value = "/msgType")
    @ResponseBody
    public ResponseEntity<?> getMsgType(@RequestParam(required = false) String msgCode, @RequestParam(required = false) String msgDesc) {
        return new ResponseEntity<>(dropdownService.getMsgType(msgCode, msgDesc), HttpStatus.OK);
    }

    @GetMapping(value = "/system-params")
    @ResponseBody
    public ResponseEntity<?> getSystemParams(@RequestParam(required = false) String paramName) {
        return new ResponseEntity<>(dropdownService.getSystemParams(paramName), HttpStatus.OK);
    }
    
    
}
