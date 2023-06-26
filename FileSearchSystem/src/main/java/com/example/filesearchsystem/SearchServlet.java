package com.example.filesearchsystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户提交的文件夹路径
        String folderPath = request.getParameter("folder");

        // 获取文件列表
        List<String> fileList = getFileList(folderPath);

        // 将文件列表存储在请求属性中，以便在 JSP 中进行展示
        request.setAttribute("fileList", fileList);

        // 转发到展示文件列表的 JSP 页面
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private List<String> getFileList(String folderPath) {
        List<String> fileList = new ArrayList<>();

        // 遍历文件夹下的所有文件，筛选出 pdf 和 word 文件，并将路径添加到列表中
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".pdf") || file.getName().endsWith(".doc")||file.getName().endsWith(".txt")||file.getName().endsWith(".docx"))) {
                    fileList.add(file.getAbsolutePath());
                }
            }
        }

        return fileList;
    }
}
