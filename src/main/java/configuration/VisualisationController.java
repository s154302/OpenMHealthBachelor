package configuration;

import common.CurrentUsers;
import dsu.service.DsuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VisualisationController {

    @Autowired
    private DsuService dsuService;

    /**
     * Mapping for visualising the sychronised data.
     * @param username name of the user to visualise data for
     * @return a view for the visualisation of the data
     */
    @RequestMapping(value = "/VisualizeData", method=RequestMethod.POST)
    public ModelAndView VisualizeData(@RequestParam("username") String username) {
        ModelAndView result;
        System.out.println(username);
        result = new ModelAndView("visualization");
        String json = "";
        try {
            String accessToken = CurrentUsers.getUsers().get(username).getAccessToken();
            json = dsuService.jsonReader(accessToken);
            if(json.equals("[]")) {
                return new ModelAndView("no_data");
            }
        } catch (Exception e) {
            return new ModelAndView("user_error");
        }
        result.addObject("jsonObject", json);
        return result;
    }

}