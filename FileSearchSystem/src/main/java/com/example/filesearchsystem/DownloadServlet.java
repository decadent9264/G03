package com.example.filesearchsystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] filenames = request.getParameterValues("filenames");
        String keyword = request.getParameter("keyword");
        String format = request.getParameter("format");

        // 创建临时文件夹
        File tempFolder = new File(System.getProperty("java.io.tmpdir"), "filesearchsystem_temp");
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        // 创建临时文件
        File tempFile = new File(tempFolder, "search_results." + format);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        tempFile.createNewFile();

        // 将选中的结果文件内容写入临时文件
        try (FileOutputStream fos = new FileOutputStream(tempFile);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter bw = new BufferedWriter(osw)) {
            for (String filename : filenames) {
                File resultFile = new File(filename);
                if (resultFile.exists()) {
                    bw.write("文件名：" + resultFile.getName());
                    bw.newLine();
                    bw.flush();

                    try (BufferedReader br = new BufferedReader(new FileReader(resultFile))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            bw.write(line);
                            bw.newLine();
                            bw.flush();
                        }
                    }
                }
            }
        }

        // 设置响应头
        if (format.equals("html")) {
            response.setContentType("text/html");
        } else if (format.equals("txt")) {
            response.setContentType("text/plain");
        }
        response.setHeader("Content-Disposition", "attachment; filename=search_results." + format);

        // 将临时文件的内容写入响应输出流
        try (FileInputStream fis = new FileInputStream(tempFile);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }

        // 删除临时文件
        tempFile.delete();
        tempFolder.delete();
    }
}