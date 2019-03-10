package com.content_load_sb.fileops;

import com.content_load_sb.config.Setting;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.helper.Helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by asd on 16.11.2017.
 */
public class PararelWrite extends Thread {
    public static List<HashPath> lstHashDlMachine;
    public List<HashPath> lstHashPath;

    public PararelWrite(List<HashPath> lstHashPath, List<HashPath> lstHashMachine) {
        this.lstHashPath = lstHashPath;
        PararelWrite.lstHashDlMachine = lstHashMachine;
    }

    @Override
    public synchronized void run() {
        // TODO Auto-generated method stub
        super.run();
        for (HashPath hashPath : lstHashPath) {
            try {
                String sourcesPath = Setting.getInstance().getSourceDiskRootPath() + hashPath.getPathTail();
                String destinationPath = Setting.getInstance().getTargetDiskRootPath() + hashPath.getPathTail();

                if (Files.exists(Paths.get(destinationPath))) {
                    try {
                        Files.deleteIfExists(Paths.get(destinationPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (!Files.exists(Paths.get(destinationPath).getParent())) {
                    new File(destinationPath).getParentFile().mkdirs();
                }
                Helper.getInstance().fileCopy(new File(sourcesPath), new File(destinationPath));
                lstHashDlMachine.add(hashPath);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
