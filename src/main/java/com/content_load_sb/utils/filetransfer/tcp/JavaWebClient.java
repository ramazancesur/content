package com.content_load_sb.utils.filetransfer.tcp;
/*

import com.content_load_sb.config.Setting;
import com.content_load_sb.dto.HashPath;
import com.content_load_sb.helper.EnumUtil;
import com.content_load_sb.helper.Helper;
import com.content_load_sb.persistmysql.entity.TargetFile;
import com.content_load_sb.service.interfaces.ITargetFileService;
import com.content_load_sb.utils.filetransfer.BandWinthControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

*/
/**
 * Created by ramazancesur on 11/18/17.
 *//*

@Component
public class JavaWebClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaWebClient.class);
    Setting setting = Setting.getInstance();
    Helper helper = Helper.getInstance();
    @Autowired
    private ITargetFileService targetFileService;
    private Socket s;
    private BandWinthControl bandWinthControl;

    public JavaWebClient() {
        try {

            String host = setting.getNetworkDisk();
            int port = setting.getSocketPort();
            if (internetControl() == true) {
                s = new Socket(host, port);
                bandWinthControl = new BandWinthControl();
            } else {
                System.out.println("Internet Connection is Lost...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean internetControl() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface interf = interfaces.nextElement();
            if (interf.isUp() && !interf.isLoopback())
                return true;
        }

        return false;
    }

    // Çoklu dosya gönderimi
    public void calculateAndSendData() {
        LOGGER.debug("calculate send data function started");
        List<TargetFile> lstTargetFile = targetFileService.getAll();
        String hashJson = helper.readFileJson(Setting.getInstance().getDlHashFileLocation());
        List<HashPath> lstHashPath = helper.jsonToData(HashPath.class, hashJson);
        synchronizedData(lstHashPath, lstTargetFile);
        LOGGER.debug("calculate send data function finished");
    }

    private synchronized void synchronizedData(List<HashPath> lstDlMachinesData, List<TargetFile> lstTargetFile) {
        List<HashPath> lstMySQLHash = lstTargetFile.parallelStream().map(x -> {
            HashPath hashPath = new HashPath();
            hashPath.setPathTail(x.getPathTAIL());
            hashPath.setHash(x.getHash());
            return hashPath;
        }).collect(Collectors.toList());

        // Bu data eklenecek olan datadır
        List<HashPath> lstAddetedData = Helper.getInstance().diffirentTwoList(lstDlMachinesData, lstMySQLHash);
        lstAddetedData.stream().forEach(hashPath -> {
            File file = new File(helper.getFileParentPathTail(hashPath.getPathTail()));
            try {
                sendFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("while file sending occured error " + e.getMessage());
            }
        });
        List<HashPath> lstRemovedFile = helper.diffirentTwoList(lstMySQLHash, lstDlMachinesData);
        String removedDataFolder = Setting.getInstance().getResourceFolder() + "/removedFile.kife";
        helper.writeJson(lstRemovedFile, removedDataFolder);

        try {
            sendFile(new File(removedDataFolder));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("RemovedFile.kife file sending when occured error " + e.getMessage());
        }
    }

    //  BURADAKİ TEK DOSYA GONDERMEK İÇİN YAZILMIŞTIR
    public synchronized void sendFile(File file) throws IOException {
        try {
            TargetFile targetFile = targetFileService.createFileData(file.toPath());
            System.out.println("database insertion...");
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Long bandWith = Helper.getInstance().convertToByte(
                    Setting.getInstance().getFileUploadLimit(), EnumUtil.MaxUploadSize.MB);

            if (bandWinthControl.bandWidthLimit(bandWith, file, dos)) {
                System.out.println("Data Stream is complate...");
            } else {
                targetFileService.remove(targetFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

*/
