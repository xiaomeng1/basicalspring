package xiangli.service;

import xiangli.annotation.XMService;

@XMService
public class UserService implements IUserService {

    private IPrinterService printerService;

    public String loadUserName(String userName) {
        return "my name is " + userName;
    }
}
