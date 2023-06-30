package com.example.filesearchsystem;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
                if (file.isFile() && (file.getName().endsWith(".txt") || file.getName().endsWith(".pdf"))) {
                    // 读取文件内容进行检索
                    List<String> lines = searchFileContent(file, keyword);
                    // 将匹配行的内容存储在列表中
                    matchedLines.addAll(lines);
                } else if (file.getName().endsWith(".doc")) {
                    try (FileInputStream fis = new FileInputStream(file);
                         HWPFDocument doc = new HWPFDocument(fis)) {
                        WordExtractor extractor = new WordExtractor(doc);
                        String[] paragraphs = extractor.getParagraphText();
                        matchedLines.add(System.lineSeparator());
                        matchedLines.add("文件名：" + file.getName());
                        matchedLines.add("-------------------------");
                        int lineNumber = 1;
                        for (String paragraph : paragraphs) {
                            if (paragraph.contains(keyword)) {
                                matchedLines.add("行号：" + lineNumber + "，内容：" + paragraph);
                            }
                            lineNumber++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (file.getName().endsWith(".docx")) {
                    try (FileInputStream fis = new FileInputStream(file);
                         XWPFDocument doc = new XWPFDocument(fis)) {
                        List<XWPFParagraph> paragraphs = doc.getParagraphs();
                        matchedLines.add(System.lineSeparator());
                        matchedLines.add("文件名：" + file.getName());
                        matchedLines.add("-------------------------");
                        int lineNumber = 1;
                        for (XWPFParagraph paragraph : paragraphs) {
                            String text = paragraph.getText();
                            if (text.contains(keyword)) {
                                matchedLines.add("行号：" + lineNumber + "，内容：" + text);
                            }
                            lineNumber++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return matchedLines;
    }

    private List<String> searchFileContent(File file, String keyword) {
        List<String> matchedLines = new ArrayList<>();
        if (file.getName().endsWith(".txt")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                String line;
                matchedLines.add(System.lineSeparator());
                matchedLines.add("文件名：" + file.getName());
                System.out.println(file.getName());
                matchedLines.add("-------------------------");
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line.toString());
                    if (line.indexOf(keyword) >= 0) {
                        matchedLines.add("行号：" + lineNumber + "，内容：" + line);
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.getName().endsWith(".pdf")) {
            try (PDDocument pdfDoc = PDDocument.load(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String content = stripper.getText(pdfDoc);
                String[] lines = content.split("\\r?\\n"); // 将提取的文本按行拆分
                matchedLines.add(System.lineSeparator());
                matchedLines.add("文件名：" + file.getName());
                matchedLines.add("-------------------------");
                int lineNumber = 1;
                for (String line : lines) {
                    if (line.contains(keyword)) {
                        matchedLines.add("行号：" + lineNumber + "，内容：" + line);
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return matchedLines;
    }
}