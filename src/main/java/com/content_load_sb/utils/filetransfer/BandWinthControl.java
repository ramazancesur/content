package com.content_load_sb.utils.filetransfer;

import com.google.common.util.concurrent.RateLimiter;

import java.io.*;

/**
 * Created by ramazancesur on 11/19/17.
 */
public class BandWinthControl {
    public boolean bandWidthLimit(Long uploadSpeed, File file, DataOutputStream outputStream) throws IOException {
        final RateLimiter rateLimiter = RateLimiter.create(uploadSpeed);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];

            while (fis.read(buffer) > 0) {
                rateLimiter.tryAcquire(buffer.length);
                outputStream.write(buffer);
            }
            fis.close();
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }


    public void bandWithLimitter(OutputStream outputStream, File file, Long speed) throws IOException, InterruptedException {
        int BUF_SIZE = 1024;
        //    long speed = 12800L; // 100 Kbit/s = 100 * 1024 / 8 Byte/s = 12800 Byte/s
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[BUF_SIZE];
        long cur = 0L;
        int n = 0;
        long startTime = System.currentTimeMillis();
        while ((n = fis.read(buf)) != -1) {
            cur += n;
            outputStream.write(buf);
            if (cur > speed) {
                long idleTime = (cur - speed) * 1000 / speed;
                Thread.sleep(idleTime);
                cur = 0;
                startTime = System.currentTimeMillis();
            }
        }
        fis.close();
        outputStream.flush();
        outputStream.close();
    }
}