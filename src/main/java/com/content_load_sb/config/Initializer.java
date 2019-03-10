package com.content_load_sb.config;

import com.content_load_sb.fileops.TransferMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by asd on 8.11.2017.
 */

@Component
public class Initializer implements ApplicationRunner {

    @Autowired
    private TransferMain transferMain;


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        transferMain.transferStart();
    }
}
