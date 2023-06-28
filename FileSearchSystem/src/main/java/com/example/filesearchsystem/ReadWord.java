package com.example.filesearchsystem;

import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ReadWord")
public class ReadWord extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户提交的文件夹路径和关键词
        String folderPath = request.getParameter("folderPath");
        String keyword = request.getParameter("keyword");

        File folder = new File(folderPath);
        List<String> searchResults = new ArrayList<>();

        // 递归遍历文件夹下的所有文件
        traverseFiles(folder, keyword, searchResults);

        // 将搜索结果传递给readword.jsp页面显示
        request.setAttribute("searchResults", searchResults);
        request.getRequestDispatcher("readword.jsp").forward(request, response);
    }

    // 递归遍历指定文件夹下的所有文件，并查找包含关键词的Word文件
    private void traverseFiles(File folder, String keyword, List<String> searchResults) {
        if (folder.isDirectory()) {
            // 遍历文件夹中的所有文件
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 如果是子文件夹，则递归遍历
                        traverseFiles(file, keyword, searchResults);
                    } else if (file.isFile() && file.getName().endsWith(".doc")) {
                        // 如果是Word文件，则读取文件内容并查找关键词
                        try {
                            FileInputStream in = new FileInputStream(file);
                            byte[] fileBytes = FileUtils.readFileToByteArray(file);
                            Class<?> extractorClass = Class.forName("org.textmining.extractor.TextExtractor");
                            Method extractTextMethod = extractorClass.getMethod("extractText", byte[].class);
                            String fileContent = (String) extractTextMethod.invoke(null, fileBytes);
                            if (fileContent.contains(keyword)) {
                                // 获取文件路径和内容行号
                                String filePath = file.getAbsolutePath();
                                List<Integer> lineNumbers = findKeywordLines(fileContent, keyword);

                                // 将搜索结果添加到列表中
                                StringBuilder result = new StringBuilder();
                                result.append(filePath).append("\n");
                                result.append("-----------------------------------").append("\n");
                                for (int lineNumber : lineNumbers) {
                                    result.append("行号").append(lineNumber).append("\t").append(getLineContent(fileContent, lineNumber)).append("\n");
                                }
                                searchResults.add(result.toString());
                            }
                            in.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // 查找包含关键词的内容行的行号
    private List<Integer> findKeywordLines(String fileContent, String keyword) {
        List<Integer> lineNumbers = new ArrayList<>();
        String[] lines = fileContent.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(keyword)) {
                lineNumbers.add(i + 1); // 行号从1开始计数
            }
        }
        return lineNumbers;
    }

    // 获取指定行号的内容行
    private String getLineContent(String fileContent, int lineNumber) {
        String[] lines = fileContent.split("\n");
        if (lineNumber >= 1 && lineNumber <= lines.length) {
            return lines[lineNumber - 1];
        }
        return "";
    }
}