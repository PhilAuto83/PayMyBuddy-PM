package com.phildev.pmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransferManagementController {

    @GetMapping("/transfer-management")
    public String renderTransferManagementPage(){
        return "transfer-management";
    }
}
