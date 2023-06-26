<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>APP文件检索系统</title>
  <script>
    function chooseFolder() {
      // 创建一个隐藏的文件选择框
      var input = document.createElement('input');
      input.type = 'file';
      input.setAttribute('directory', 'true');
      input.setAttribute('webkitdirectory', 'true');
      input.setAttribute('mozdirectory', 'true');
      input.setAttribute('msdirectory', 'true');
      input.setAttribute('odirectory', 'true');
      input.setAttribute('multiple', 'false');
      input.style.display = 'none';

      // 添加文件选择框到页面
      document.body.appendChild(input);

      // 监听选择目录的变化事件
      input.addEventListener('change', function(e) {
        // 获取选择的目录路径
        var folderPath = e.target.files[0].path;
        // 执行后续操作，比如发送路径到后端进行处理

        // 移除文件选择框
        document.body.removeChild(input);
      });

      // 模拟点击文件选择框
      input.click();
    }
  </script>
</head>
<body>
<h1>选择查询目录</h1>
<button onclick="chooseFolder()">选择目录</button>
</body>
</html>
