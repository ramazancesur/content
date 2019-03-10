package com.content_load_sb.utils.filetransfer.samba;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asd on 19.11.2017.
 */
public class SharedFileSend {

    public String sendData(File localFile, String sharedRemoteUrl) {

        String createdFileRemoteUrl = "";
//        String remotePhotoUrl = "smb://share:admin@192.168.135.11/sharedFolder/"; //The shared directory to store pictures

        InputStream in = null;
        OutputStream out = null;
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS_");
            SmbFile remoteFile = new SmbFile(sharedRemoteUrl + "/" + fmt.format(new Date()) + localFile.getName());
            remoteFile.connect(); //Try to connect

            in = new BufferedInputStream(new FileInputStream(localFile));
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));

            byte[] buffer = new byte[4096];
            int len = 0; //Read length
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();//The refresh buffer output stream

            createdFileRemoteUrl = remoteFile.getURL().getFile();
        } catch (Exception e) {
            String msg = "The error occurred: " + e.getLocalizedMessage();
            System.out.println(msg);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }

        return createdFileRemoteUrl;
    }

    public byte[] dataDownload(String sharedDataUrl) {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            //Create a remote file object
            //String remotePhotoUrl = "smb://share:admin@192.168.135.11/sharedFolder/test.jpg";
            SmbFile remoteFile = new SmbFile(sharedDataUrl);
            remoteFile.connect(); //Try to connect
            //Create a file stream
            in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
            out = new ByteArrayOutputStream((int) remoteFile.length());
            //Read the contents of the documents
            byte[] buffer = new byte[4096];
            int len = 0; //Read length
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }

            out.flush(); //The refresh buffer output stream
            return out.toByteArray();
        } catch (Exception e) {
            String msg = "Download a remote file error: " + e.getLocalizedMessage();
            System.out.println(msg);
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                return null;
            }

        }
    }


}