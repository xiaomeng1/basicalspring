package xiangli.controller;

import xiangli.annotation.XMAutowired;
import xiangli.annotation.XMController;
import xiangli.annotation.XMParameter;
import xiangli.annotation.XMRequestMapping;
import xiangli.service.IUserService;

@XMController
@XMRequestMapping(value = "/user")
public class UserController {

    @XMAutowired
    private IUserService userService;

    @XMRequestMapping("/load")
    public String loadUserName(@XMParameter(name = "userName") String userName) {
        return userService.loadUserName(userName);
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
