<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>文本文件内容检索</title>
</head>
<body>
<h1>文本文件内容检索</h1>
<form action="SearchServlet" method="post">
    <label for="folderPath">文件夹路径：</label>
    <input type="text" id="folderPath" name="folderPath" value="C:\\Users\\86182\\Desktop\\jjjj" required readonly>
    <br>
    <label for="keyword">关键词：</label>
    <input type="text" id="keyword" name="keyword" required>
    <button type="submit">开始检索</button>
</form>
</body>
</html>