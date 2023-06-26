<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>显示盘符</title>
</head>
<body>
<br/>
<h2>盘符列表：</h2>
<%-- 在这里显示盘符 --%>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<% List<String> drives = new ArrayList<>();
  File[] roots = File.listRoots();
  for (File root : roots) {
    drives.add(root.getPath());
  }
  for (String drive : drives) { %>
<p><%= drive %><br>
  <input type="file" id="folder" webkitdirectory="true" directory="true" onchange="handleFolderSelection(event)">
<ul id="fileList"></ul>

<script>
  function handleFolderSelection(event) {
    var folderInput = event.target;
    var files = folderInput.files;
    var fileList = document.getElementById('fileList');

    // 清空文件列表
    fileList.innerHTML = '';

    // 遍历选择的文件夹中的文件
    for (var i = 0; i < files.length; i++) {
      var file = files[i];
      var listItem = document.createElement('li');
      listItem.textContent = file.name;
      fileList.appendChild(listItem);
    }
  }
</script> </p>
<% } %>
</body>
</html>
