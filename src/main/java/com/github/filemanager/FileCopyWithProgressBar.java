package com.github.filemanager;

import java.io.*;
import javax.swing.*;

public class FileCopyWithProgressBar extends SwingWorker<Void, Integer> {
    
    private File sourceFile;
    private File destFile;
    private JProgressBar progressBar;
    private JFrame fram;
    
    public FileCopyWithProgressBar(File sourceFile, File destFile, JProgressBar progressBar,JFrame fram) {
        this.sourceFile = sourceFile;
        this.destFile = destFile;
        this.progressBar = progressBar;
        this.fram = fram;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);
            long totalBytes = sourceFile.length();
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            long totalBytesRead = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                int percentComplete = (int) (totalBytesRead * 100 / totalBytes);
                publish(percentComplete);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return null;
    }
    
    @Override
    protected void process(java.util.List<Integer> chunks) {
        for (int percentComplete : chunks) {
            progressBar.setValue(percentComplete);
        }
    }
    
    @Override
    protected void done() {
        progressBar.setValue(100);
        fram.dispose();
        JOptionPane.showMessageDialog(null, "File copied successfully!");
    }
}
