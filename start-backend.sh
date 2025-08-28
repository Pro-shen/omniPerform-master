#!/bin/bash

echo "🚀 启动 OmniPerform 后端服务..."
echo "================================"

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到Java环境，请先安装JDK 1.8+"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: 未找到Maven环境，请先安装Maven 3.6+"
    exit 1
fi

# 进入后端项目目录
cd "$(dirname "$0")/omniperform-web"

if [ ! -f "pom.xml" ]; then
    echo "❌ 错误: 未找到pom.xml文件，请检查项目结构"
    exit 1
fi

echo "📦 项目目录: $(pwd)"
echo "☕ Java版本: $(java -version 2>&1 | head -n 1)"
echo "🔧 Maven版本: $(mvn -version 2>&1 | head -n 1)"
echo ""

echo "🔄 编译项目..."
mvn clean compile -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ 编译失败，请检查代码"
    exit 1
fi

echo ""
echo "✅ 编译成功!"
echo "🚀 启动服务..."
echo ""
echo "📍 服务地址: http://localhost:8080/api"
echo "📍 健康检查: http://localhost:8080/api/health"
echo "📍 前端测试页面: ../website3.0/test-connection.html"
echo ""
echo "按 Ctrl+C 停止服务"
echo "================================"

# 启动Spring Boot应用
mvn spring-boot:run
