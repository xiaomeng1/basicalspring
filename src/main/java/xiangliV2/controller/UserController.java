package xiangliV2.controller;

import xiangliV2.annotation.XMAutowired;
import xiangliV2.annotation.XMController;
import xiangliV2.annotation.XMParameter;
import xiangliV2.annotation.XMRequestMapping;
import xiangliV2.service.IUserService;
import xiangliV2.webmvc.servlet.XMModelAndView;

import java.util.HashMap;

@XMController
@XMRequestMapping(value = "/user")
public class UserController {

    @XMAutowired
    private IUserService userService;

    @XMRequestMapping("/load")
    public XMModelAndView loadUserName(@XMParameter(name = "userName") String userName) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("teacher", userName);
        hashMap.put("data", "i am xiaomeng.");
        hashMap.put("token", "i am xiaomeng token.");
        userService.loadUserName(userName);
       return new XMModelAndView("first.html", hashMap);
    }


    @XMRequestMapping("/create")
    public String createUser(@XMParameter(name = "userName") String userName) {
        return userService.loadUserName(userName);
    }


    @XMRequestMapping("/delete")
    public String deleteUser(@XMParameter(name = "userName") String userName) {
        return userService.loadUserName(userName);
    }
}
