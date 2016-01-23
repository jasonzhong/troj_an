package com.rapid.jason.rapidnetwork.FreeLoad.toolbox;

import com.rapid.jason.rapidnetwork.FreeLoad.core.Prepare;
import com.rapid.jason.rapidnetwork.FreeLoad.core.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrepareDownload implements Prepare {

    /** connect to get downloadfile head timeout count. */
    private final static int CONNECT_TIMEOUT = 5 * 1000;

    @Override
    public boolean preparePerform(Request<?> request) {
        long downloadLength = getFileSize(request);
        if (downloadLength <= 0) {
            return false;
        }
        request.setDownloadLength(downloadLength);

        File saveFile = createFile(request);
        if (saveFile == null) {
            return false;
        }
        request.setDownloadFile(saveFile);

        return true;
    }

    private File createFile(Request<?> request) {
        if (!createFolder(request.getFolderName())) {
            return null;
        }

        File saveFile = new File(request.getFolderName(), request.getFileName());

        try {
            RandomAccessFile randOut = new RandomAccessFile(saveFile, "rw");
            if(request.getDownloadLength() > 0) {
                randOut.setLength(request.getDownloadLength());
            }
            randOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return saveFile;
    }

    private boolean createFolder(String fileFolder) {
        File folder = new File(fileFolder);
        if (!createAndCheckFolder(folder)) {
            return false;
        }

        if (!folder.isDirectory()) {
            if (!folder.delete()) {
                return false;
            }
        }

        if (!createAndCheckFolder(folder)) {
            return false;
        }

        return true;
    }

    private boolean createAndCheckFolder(File folder) {
        if (!folder.exists()) {
            folder.mkdirs();
        } else {
            return true;
        }

        if (!folder.exists()) {
            return false;
        }
        return true;
    }

    private long getFileSize(Request<?> request) {
        if (request.getDownloadLength() > 0) {
            return 0;
        }

        long downloadLength = 0;
        try {
            downloadLength = connectAndGetFileSize(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return downloadLength;
    }

    private long connectAndGetFileSize(Request<?> request) throws Exception {
        URL mUrl = new URL(request.getUrl());
        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept-Encoding", "identity");
        conn.setRequestProperty("Referer", request.getUrl());
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.connect();

        long lenght = 0;
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            lenght = conn.getContentLength();
        }
        conn.disconnect();

        return lenght;
    }
}
