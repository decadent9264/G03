<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>APP文件检索系统</title>
    <script>
        function handleFolderSelection(event) {
            var selectedFolder = event.target.files[0];
            if (selectedFolder) {
                var folderPath = selectedFolder.webkitRelativePath.split("/").slice(0, -1).join("/");
                sessionStorage.setItem('folderPath', folderPath);
                document.getElementById("folderPath").value = "I:"+"\\"+folderPath;
            }
        }

        window.onload = function() {
            var folderPath = sessionStorage.getItem('folderPath');
            if (folderPath) {
                document.getElementById("folderPath").value = "I:"+"\\"+folderPath;
            }
        }
    </script>
</head>
<body>
<h1>APP文件检索系统</h1>
<form action="SearchServlet" method="post">
    <label for="folder">选择文件夹:</label>
    <input type="file" id="folder" name="folder" webkitdirectory="" directory="" onchange="handleFolderSelection(event)" required>
    <input type="text" id="folderPath" name="folderPath" required readonly><br>
    <label for="keyword">输入关键词:</label>
    <input type="text" id="keyword" name="keyword" required>
    <button type="submit">开始检索</button>
</form>

<!-- 显示检索结果 -->
<h2>检索结果：</h2>
    <%
        List<String> searchResult = (List<String>) request.getAttribute("searchResult");
        if (searchResult != null && !searchResult.isEmpty()) {
            for (String line : searchResult) {
    %>
    <p><%= line %></p>
    <%
        }
    }
    %>
</body>
</html>