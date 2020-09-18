package xiangliV2.service;

import xiangliV2.annotation.XMService;
import xiangliV2.annotation.XMAutowired;

@XMService("XMPrinterService")
public class PrinterService implements IPrinterService {

    @XMAutowired
    private IUserService userService;
}
