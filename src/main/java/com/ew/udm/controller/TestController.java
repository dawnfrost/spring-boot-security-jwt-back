package com.ew.udm.controller;

import com.ew.udm.controller.res.TestResponse;
import com.ew.udm.models.user.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    public TestController() {
    }

    @ResponseBody
    @RequestMapping(value = "/method-get/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public Object testGet(@PathVariable int pageNum, @PathVariable int pageSize) {
        return new TestResponse(1, 22L, "asdasddasdsa", 3.14);
    }
}
