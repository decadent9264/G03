<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        .highlight {
            background-color: red;
        }
    </style>
    <title>APP文件检索系统</title>
    <script>
        function handleFolderSelection(event) {
            var selectedFolder = event.target.files[0];
            if (selectedFolder) {
                var folderPath = selectedFolder.webkitRelativePath.split("/").slice(0, -1).join("/");
                sessionStorage.setItem('folderPath', folderPath);
                document.getElementById("folderPath").value = "I:" + "\\" + folderPath;
            }
        }

        window.onload = function() {
            var folderPath = sessionStorage.getItem('folderPath');
            if (folderPath) {
                document.getElementById("folderPath").value = "I:" + "\\" + folderPath;
            }
        }

        function submitForm() {
            var keyword = document.getElementById("keyword").value;
            sessionStorage.setItem('keyword', keyword);
        }

        function restoreKeyword() {
            var keyword = sessionStorage.getItem('keyword');
            if (keyword) {
                document.getElementById("keyword").value = keyword;
            }
        }

        function downloadResults() {
            var checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
            if (checkboxes.length === 0) {
                alert("请至少选择一项进行下载！");
                return;
            }
            var results = "";
            for (var i = 0; i < checkboxes.length; i++) {
                var checkbox = checkboxes[i];
                var result = checkbox.parentNode.textContent.trim();
                results += result + "\n";
            }
            var keyword = document.getElementById("keyword").value;
            var highlightedResults = results.replace(new RegExp(keyword, 'g'), '<span style="background-color: yellow;">' + keyword + '</span>');
            var element = document.createElement('a');
            element.setAttribute('href', 'data:text/html;charset=utf-8,' + encodeURIComponent(highlightedResults));
            element.setAttribute('download', 'search_results.html');
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        }

        function downloadResultsTXT() {
            var checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
            if (checkboxes.length === 0) {
                alert("请至少选择一项进行下载！");
                return;
            }
            var results = "";
            for (var i = 0; i < checkboxes.length; i++) {
                var checkbox = checkboxes[i];
                var result = checkbox.parentNode.textContent.trim();
                results += result + "\n";
            }
            var keyword = document.getElementById("keyword").value;
            var highlightedResults = results.replace(new RegExp(keyword, 'g'), keyword);

            var element = document.createElement('a');
            element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(highlightedResults));
            element.setAttribute('download', 'search_results.txt');
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        }
    </script>
</head>
<body>
<h1>APP文件检索系统</h1>
<form action="SearchServlet" method="post" onsubmit="submitForm()">
    <label for="folder">选择文件夹:</label>
    <input type="file" id="folder" name="folder" webkitdirectory="" directory="" onchange="handleFolderSelection(event)" required>
    <input type="text" id="folderPath" name="folderPath" required readonly><br>
    <label for="keyword">输入关键词:</label>
    <input type="text" id="keyword" name="keyword" required>
    <button type="submit">开始检索</button>
</form>

<!-- 显示检索结果 -->
<h2>检索结果：</h2>
<div id="searchResults">
    <%
        List<String> searchResult = (List<String>) request.getAttribute("searchResult");
        if (searchResult != null && !searchResult.isEmpty()) {
            for (int i = 0; i < searchResult.size(); i++) {
                String line = searchResult.get(i);
                // 判断当前行是否是文件名
                if (line.startsWith("文件名：")) {
                    // 使用正则表达式将文件名提取出来
                    String fileName = line.replaceAll("文件名：", "").trim();
    %>
    <p>
        <input type="checkbox" name="selectedFiles" value="<%= fileName %>">
        <%= line %>
    </p>
    <%
    } else {
    %>
    <p><%= line %></p>
    <%
                }
            }
        }
    %>
</div>

<!-- 下载按钮 -->
<button onclick="downloadResults()">下载选中结果为HTML</button>
<button onclick="downloadResultsTXT()">下载选中结果为TXT</button>
<script>
    restoreKeyword(); // 恢复关键词文本框的值

    function downloadResults() {
        var checkboxes = document.querySelectorAll('input[name="selectedFiles"]:checked');
        if (checkboxes.length === 0) {
            alert("请至少选择一项进行下载！");
            return;
        }
        var results = "";
        for (var i = 0; i < checkboxes.length; i++) {
            var checkbox = checkboxes[i];
            var result = checkbox.parentNode.textContent.trim();
            results += result + "\n";
        }
        var keyword = document.getElementById("keyword").value;
        var highlightedResults = results.replace(new RegExp(keyword, 'g'), '<span style="background-color: yellow;">' + keyword + '</span>');
        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/html;charset=utf-8,' + encodeURIComponent(highlightedResults));
        element.setAttribute('download', 'search_results.html');
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    }

    function downloadResultsTXT() {
        var checkboxes = document.querySelectorAll('input[name="selectedFiles"]:checked');
        if (checkboxes.length === 0) {
            alert("请至少选择一项进行下载！");
            return;
        }
        var results = "";
        for (var i = 0; i < checkboxes.length; i++) {
            var checkbox = checkboxes[i];
            var result = checkbox.parentNode.textContent.trim();
            var lines = result.split("\n");
            var fileName = checkbox.value;
            var nextElement = checkbox.parentNode.nextElementSibling;
            for (var j = 0; j < lines.length; j++) {
                var line = lines[j];
                if (line.startsWith("文件名：")) {
                    results += line + "\n";
                    while (nextElement.textContent.trim().startsWith("-------------------------") || nextElement.textContent.trim().startsWith("行号：")) {
                        results += nextElement.textContent.trim() + "\n";
                        nextElement = nextElement.nextElementSibling;
                    }
                } else {
                    results += line + "\n\n";
                }
            }
        }
        var keyword = document.getElementById("keyword").value;
        var highlightedResults = results.replace(new RegExp(keyword, 'g'), keyword);

        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(highlightedResults));
        element.setAttribute('download', 'search_results.txt');
        element.style.display = 'none';
        document.body.appendChild(element);
        element.click();
        document.body.removeChild(element);
    }
</script>

</body>
</html>