package dsu.controller;

import common.service.UserService;
import dsu.service.DsuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DsuController {
    @Autowired
    private DsuService dsuService;

    @Autowired
    private UserService userService;


    @GetMapping("/DsuGetData")
    public ResponseEntity DsuGetData(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam String dataType) throws Exception {
        HttpHeaders responseHeader = new HttpHeaders();
        Map<String, Object> responseMap = new HashMap<>();
        try {
            String data = dsuService.getData(userService.getAccessToken(username, password), dataType);
            responseMap.put("body", data);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>(responseMap, responseHeader, HttpStatus.OK);
    }

}
