package com.cz.wdpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class FileContentExtractor {
    public List<String> extractContentFromFolder(String folderPath) {
        List<String> contentList = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

                if (fileExtension.equalsIgnoreCase("doc")) {
                    // 读取 Word 文件内容
                    try (FileInputStream fis = new FileInputStream(file);
                         XWPFDocument sourceDoc = new XWPFDocument(fis)) {

                        // 复制源文档的段落到目标文档
                        for (XWPFParagraph paragraph : sourceDoc.getParagraphs()) {
                            contentList.add(paragraph.getText());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (fileExtension.equalsIgnoreCase("pdf")) {
                    // 读取 PDF 文件内容
                    try (FileInputStream fis = new FileInputStream(file);
                         PDDocument pdfDoc = PDDocument.load(fis)) {
                        PDFTextStripper stripper = new PDFTextStripper();
                        String content = stripper.getText(pdfDoc);
                        contentList.add(content);
                        System.out.println("ssss");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return contentList;
    }

    public void saveContentToWord(List<String> contentList, String outputPath) {
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(outputPath)) {

            for (String content : contentList) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun run = paragraph.createRun();
                run.setText(content);
            }

            document.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
