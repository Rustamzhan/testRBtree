package com.testRBtree.RBTree;

import RBtree.MyRedBlackTree;
import RBtree.RBTreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@RestController
public class IndexController {

    MyRedBlackTree tree = MyRedBlackTree.getInstance();

    @RequestMapping("/")
    public ModelAndView index() {
        Map<String, String> model = new HashMap<>();
        model.put("value","");
        model.put("color", "");
        return new ModelAndView("index", model);
    }

    @PostMapping(value = "/jsonListener")
    public ResponseEntity<?> jsonSender(@RequestBody String request) {
        ObjectMapper mapper = new ObjectMapper();
        int value = Integer.parseInt(request.split("\"")[3]);
        synchronized (tree) {
            tree.addNode(value);
        }
        String result = "";
        try (StringWriter writer = new StringWriter()) {
            mapper.writeValue(writer, tree);
            result = writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/jsonListener/del")
    public ResponseEntity<?> jsonSenderDel(@RequestBody String request) {
        ObjectMapper mapper = new ObjectMapper();
        int value = Integer.parseInt(request.split("\"")[3]);
        synchronized (tree) {
            tree.deleteNode(value);
        }
        String result = "";
        try (StringWriter writer = new StringWriter()) {
            mapper.writeValue(writer, tree);
            result = writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
