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
<form action="ReadWord" method="post">
  <label for="folder">选择文件夹:</label>
  <input type="file" id="folder" name="folder" webkitdirectory="" directory="" onchange="handleFolderSelection(event)" required>
  <input type="text" id="folderPath" name="folderPath" required readonly><br>
  <label for="keyword">输入关键词:</label>
  <input type="text" id="keyword" name="keyword" required>
  <button type="submit">开始检索</button>
</form>

<%-- 显示检索结果 --%>
<h2>检索结果：</h2>
<% List<String> searchResults = (List<String>) request.getAttribute("searchResults"); %>
<% if (searchResults != null && searchResults.size() > 0) { %>
<% for (String result : searchResults) { %>
<% String[] lines = result.split("\n"); %>
<p><%= lines[0] %></p>
<hr>
<ul>
  <% for (int i = 2; i < lines.length; i++) { %>
  <% String[] lineParts = lines[i].split("\t"); %>
  <li>行号<%= lineParts[0].substring(2) %>: <%= lineParts[1] %></li>
  <% } %>
</ul>
<% } %>
<% } else { %>
<p>没有找到匹配的结果。</p>
<% } %>
</body>
</html>