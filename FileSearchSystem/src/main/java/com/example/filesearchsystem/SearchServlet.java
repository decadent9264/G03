package com.example.filesearchsystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户提交的文件夹路径和关键词
        String folderPath = request.getParameter("folderPath");
        String keyword = request.getParameter("keyword");

        // 进行文件内容检索
        List<String> searchResult = performSearch(folderPath, keyword);

        // 将检索结果存储在请求属性中，以便在 JSP 中进行展示
        request.setAttribute("searchResult", searchResult);

        // 转发到展示检索结果的 JSP 页面
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private List<String> performSearch(String folderPath, String keyword) {
        List<String> matchedLines = new ArrayList<>();

        // 遍历文件夹下的所有文件，进行内容检索
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".txt") || file.getName().endsWith(".doc") || file.getName().endsWith(".docx"))) {
                    // 读取文件内容进行检索
                    List<String> lines = searchFileContent(file, keyword);
                    // 将匹配行的内容存储在列表中
                    matchedLines.addAll(lines);
                }
            }
        }

        return matchedLines;
    }

    private List<String> searchFileContent(File file, String keyword) {
        List<String> matchedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            matchedLines.add("文件名：" + file.getName());
            System.out.println(file.getName());
            matchedLines.add(System.lineSeparator());
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {

                if (line.contains(keyword)) {
                    matchedLines.add( "行号：" + lineNumber + "内容：" + line);
                    matchedLines.add(System.lineSeparator());
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchedLines;
    }
}