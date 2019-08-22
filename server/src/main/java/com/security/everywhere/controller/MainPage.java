package com.security.everywhere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainPage {

    @GetMapping("/main")
    public String getTesting(Model model){

        return "main";
    }





    @GetMapping("/content/{contentId}")
    public String Contentpage(@PathVariable String contentId, Model model) {
        //System.out.println("content");
        return "content";  // html name
    }

    @GetMapping("/content/tourcontent/{contentId}/{x}/{y}")
    public String TourContentpage(@PathVariable String contentId,String x,String y , Model model) {
        //System.out.println("content");
        return "tourcontent";  // html name
    }
    @GetMapping("/{contentId}")
    public String TourContentpage3(@PathVariable String contentId, Model model) {
        //System.out.println("content");
        return "tourcontent";  // html name
    }



//    @GetMapping("/loginPage")
//    public String loginPage() { return "loginPage"; }

//    @PostMapping("/")
//    public String postTesting(TestParam testParam, Model model) {
////        model.addAttribute("name", testParam.getClass().getName());
//        model.addAttribute("name", testParam.toString());
//        return "test";
//    }
}
