package common.controller;

import common.CurrentUsers;
import common.PropertiesLoad;
import common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
class UserController {
    private final UserService USER_SERVICE;

    @Autowired
    public UserController(UserService userService) {
        this.USER_SERVICE = userService;
    }

    /**
     * Mapping to create a user.
     * @param username name of the user to be created
     * @param password password for the created user
     * @return response entity containing the result of adding a user
     */
    @RequestMapping(value = "/create_user", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam("username") String username, @RequestParam("password") String password) {

        HttpStatus response = USER_SERVICE.addUser(username, password);
        HttpHeaders responseHeader = new HttpHeaders();
        Map<String, Object> responseMap = new HashMap<>();
        if (response == HttpStatus.OK) {
            String redirectURL = USER_SERVICE.shimmerAuth(username, PropertiesLoad.getShim());
            if (redirectURL != null) {
                responseMap.put("AuthorizationURL", redirectURL);
                return new ResponseEntity<>(responseMap, responseHeader, response);
            }
            //if redirectURL is 0 the user should be removed
            CurrentUsers.getUsers().remove(username);
            responseMap.put("Reason", "The shimmer service failed");
            return new ResponseEntity<>(responseMap, responseHeader, HttpStatus.BAD_REQUEST);
        }

        responseMap.put("Reason", "The dsu service failed");
        return new ResponseEntity<>(responseMap, responseHeader, response);
    }


}
