package com.content_load_sb.websocket.controller;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.helper.EnumUtil;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.service.interfaces.IFileService;
import com.content_load_sb.service.interfaces.ITargetFileService;
import com.content_load_sb.service.interfaces.ITargetFileSlaveService;
import com.content_load_sb.websocket.model.SambaInfo;
import com.content_load_sb.websocket.utils.FileVisitor;
import jcifs.smb.SmbFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@RestController
public class WebSocketController {
    Helper helper = Helper.getInstance();
    Setting setting = Setting.getInstance();

    @Autowired
    private IFileService fileService;

    @Autowired
    private ITargetFileService targerFileService;

    @Autowired
    private ITargetFileSlaveService targetFileSlaveService;

    @MessageMapping("/fileserver")
    @SendTo("/topic/entries")
    public SambaInfo fileserver(SambaInfo sambaInfo) throws IOException {
        Map<String, Object> propMap = Helper.getInstance().readProperties("application.properties");
        String clientIp = sambaInfo.getMachineIp();
        EnumUtil.DataBaseType dataBaseType= null;

        entryLoop :  for (Map.Entry<String, Object> entry : propMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value.toString().trim().contains(clientIp) && key.trim().contains("slaver") ){
                dataBaseType=EnumUtil.DataBaseType.MYSQL_SLAVE;
                break entryLoop;
            } else if(value.toString().trim().contains(clientIp) && key.trim().contains("mysql") ){
                dataBaseType= EnumUtil.DataBaseType.MYSQL;
                break entryLoop;
            }
        }

        dataClean(dataBaseType, sambaInfo);
/*
        File localFile = new File(filePath);
        try {
            SmbFile targetSmbFile = new SmbFile("smb://ramazancesur:Hvl2017@192.168.2.8/aaa/");
            try {
                System.out.println("Translaction is starting");
                Files.walkFileTree(localFile.toPath(), new FileVisitor(localFile.toPath(), targetSmbFile));
                System.out.println("Translaction is finishing");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }*/
        return sambaInfo;
    }

    private void dataClean(EnumUtil.DataBaseType dataBaseType, SambaInfo sambaInfo) {
        // bu bizim ana dosyamız buradaki dosya taşıma işleminden sonra düzenlenecek
        List<HashPath> lstHashDlMachine = helper.jsonToData(HashPath.class, helper.readFileJson(setting.getDlHashFileLocation()));

        String filePath = "smb://" + sambaInfo.getUserName() + ":" + sambaInfo.getPassword() + "@" + sambaInfo.getMachineIp() + "/" + sambaInfo.getSharedFolder() + "/";

        // buradaki dosya ise mysql'lerden gelecek olan datalar
        List<HashPath> lstHashDataBaseServerContent = null;

        if (dataBaseType == EnumUtil.DataBaseType.MYSQL) {
            lstHashDataBaseServerContent = targerFileService.getAllServerDataList();
        } else if (dataBaseType == EnumUtil.DataBaseType.MYSQL_SLAVE) {
            lstHashDataBaseServerContent = targetFileSlaveService.getAllServerDataList();
        }


        List<HashPath> lstDeletingData = helper.diffirentTwoList(lstHashDataBaseServerContent, lstHashDlMachine);

        lstDeletingData.parallelStream()
                .forEach(hashPath -> {
                    String currentFilePath = filePath + hashPath.getPathTail();
                    try {
                        SmbFile sambaFile = new SmbFile(currentFilePath);
                        if (sambaFile.exists()) {
                            sambaFile.delete();
                        }
                        if (dataBaseType == EnumUtil.DataBaseType.MYSQL) {
                            targerFileService.removeByPathTail(hashPath.getPathTail());
                        } else if (dataBaseType == EnumUtil.DataBaseType.MYSQL_SLAVE) {
                            targetFileSlaveService.removeByPathTAIL(hashPath.getPathTail());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


        List<HashPath> lstAddingFile= helper.diffirentTwoList(lstHashDlMachine,lstHashDataBaseServerContent);
        long totalDataSize =lstAddingFile.parallelStream().count();

        SmbFile sambaFile = null;
        try {
            sambaFile = new SmbFile(filePath);
            System.out.println("Translaction is starting");
            Files.walkFileTree(Paths.get(setting.getTargetFileLocation()), new FileVisitor(Paths.get(setting.getTargetFileLocation()), sambaFile,dataBaseType, totalDataSize ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Translaction is finishing");
    }


}
