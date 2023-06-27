<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cz.wdpdf.FileContentExtractor" %>
<%
    String folderPath = "C:\\Users\\86182\\Desktop\\jjjj";
    String outputPath = "C:\\Users\\86182\\Desktop\\sjk\\output.docx";

    // 调用后端 Java 代码处理文件内容
    FileContentExtractor fileExtractor = new FileContentExtractor();
    List<String> contentList = fileExtractor.extractContentFromFolder(folderPath);

    // 保存内容到输出文件
    fileExtractor.saveContentToWord(contentList, outputPath);
    %>
success
<%
%>
