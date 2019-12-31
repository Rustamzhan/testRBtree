package com.testRBtree.RBTree;

import RBtree.MyRedBlackTree;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

@RestController
public class IndexController {

    MyRedBlackTree tree = MyRedBlackTree.getInstance();
    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/")
    public ModelAndView index() {
        Map<String, String> model = new HashMap<>();
        model.put("value","");
        model.put("color", "");
        return new ModelAndView("index", model);
    }

    @RequestMapping(value = "/jsonListener", method = RequestMethod.POST)
    public String jsonSender(HttpServletRequest request) {
        String jsonResponse;
        try{
            InputStream in = request.getInputStream();
            Scanner s = new Scanner(in);
            String data = s.next();
            Map map = new Gson().fromJson(data, Map.class);
            int val = Integer.parseInt(map.get("value").toString());
            tree.addNode(val);
            jsonResponse = mapper.writeValueAsString(tree.getRoot());
            return jsonResponse;
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
