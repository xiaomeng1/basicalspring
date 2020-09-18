package xiangliV2.service;

import xiangliV2.annotation.XMService;
import xiangliV2.annotation.XMAutowired;

@XMService
public class UserService implements IUserService {

    @XMAutowired
    private IPrinterService printerService;

    public String loadUserName(String userName) {
        return "my name is " + userName;
    }
}
