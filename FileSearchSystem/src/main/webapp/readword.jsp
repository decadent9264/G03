<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>APP文件检索系统</title>
  <script>
    function handleFolderSelection(event) {
      var selectedFolder = event.target.files[0];
      var folderPath = selectedFolder.path;
      document.getElementById("folderPath").value = folderPath;
    }
  </script>
</head>
<body>
<h1>APP文件检索系统</h1>
<form action="ReadWord" method="post">
  <label for="folder">选择文件夹:</label>
  <input type="file" id="folder" name="folder" webkitdirectory="" directory="" onchange="handleFolderSelection(event)" required />
  <input type="hidden" id="folderPath" name="folderPath" />
  <br />
  <label for="keyword">输入关键词:</label>
  <input type="text" id="keyword" name="keyword" required />
  <button type="submit">开始检索</button>
</form>

<%-- 显示检索结果 --%>
<h2>检索结果：</h2>
<% List<String> searchResults = (List<String>) request.getAttribute("searchResult"); %>
<% if (searchResults != null && searchResults.size() > ) { %>
<ul>
  <% for (String result : searchResults) { %>
  <li><%= result %></li>
  <% } %>
</ul>
<% } else { %>
<p>没有找到匹配的结果。</p>
<% } %>
</body>
</html>