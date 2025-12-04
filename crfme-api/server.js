const express = require('express');
const mysql = require('mysql2/promise');
const cors = require('cors');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3001;

// 中间件
app.use(cors());
app.use(express.json());

// 数据库连接配置
const dbConfig = {
  host: process.env.DB_HOST,
  port: process.env.DB_PORT,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  charset: 'utf8mb4'
};

// 创建数据库连接池
const pool = mysql.createPool(dbConfig);

// 获取CRFM-E分布数据（饼图数据）
app.get('/api/crfme/distribution/:month?', async (req, res) => {
  try {
    const month = req.params.month || '2025-07'; // 默认使用最新月份
    
    const [rows] = await pool.execute(
      'SELECT score_range, count, percentage, avg_score, tier FROM member_crfme_distribution WHERE data_month = ? ORDER BY score_range',
      [month]
    );
    
    // 转换为饼图格式
    const pieData = rows.map(row => ({
      name: row.tier,
      value: row.count,
      percentage: row.percentage,
      scoreRange: row.score_range,
      avgScore: row.avg_score
    }));
    
    res.json({
      success: true,
      data: pieData,
      month: month
    });
  } catch (error) {
    console.error('获取CRFM-E分布数据失败:', error);
    res.status(500).json({
      success: false,
      message: '获取数据失败',
      error: error.message
    });
  }
});

// 获取CRFM-E条形图数据
app.get('/api/crfme/bar-chart/:month?', async (req, res) => {
  try {
    const month = req.params.month || '2025-07';
    
    const [rows] = await pool.execute(
      'SELECT score_range, count, percentage, tier FROM member_crfme_distribution WHERE data_month = ? ORDER BY CAST(SUBSTRING_INDEX(score_range, "-", 1) AS UNSIGNED)',
      [month]
    );
    
    // 转换为条形图格式
    const barData = {
      categories: rows.map(row => row.score_range),
      series: [
        {
          name: '会员数量',
          data: rows.map(row => row.count)
        },
        {
          name: '占比(%)',
          data: rows.map(row => parseFloat(row.percentage))
        }
      ],
      tiers: rows.map(row => row.tier)
    };
    
    res.json({
      success: true,
      data: barData,
      month: month
    });
  } catch (error) {
    console.error('获取CRFM-E条形图数据失败:', error);
    res.status(500).json({
      success: false,
      message: '获取数据失败',
      error: error.message
    });
  }
});

// 获取可用月份列表
app.get('/api/crfme/months', async (req, res) => {
  try {
    const [rows] = await pool.execute(
      'SELECT DISTINCT data_month FROM member_crfme_distribution ORDER BY data_month DESC'
    );
    
    const months = rows.map(row => row.data_month);
    
    res.json({
      success: true,
      data: months
    });
  } catch (error) {
    console.error('获取月份列表失败:', error);
    res.status(500).json({
      success: false,
      message: '获取数据失败',
      error: error.message
    });
  }
});

// 获取会员概览数据
app.get('/api/member/overview/:month?', async (req, res) => {
  try {
    const month = req.params.month || '2025-07'; // 默认使用最新月份
    
    // 从member_monthly_statistics表获取数据
    const [rows] = await pool.execute(
      'SELECT total_members, active_members, new_members, lost_members, repeat_purchase_rate FROM member_monthly_statistics WHERE stat_month = ?',
      [month]
    );
    
    if (rows.length === 0) {
      // 如果没有找到数据，返回默认值
      return res.json({
        success: true,
        data: {
          totalMembers: 0,
          activeMembers: 0,
          newMembers: 0,
          lostMembers: 0,
          activeRate: 0,
          churnRate: 0
        },
        month: month
      });
    }
    
    const data = rows[0];
    const activeRate = data.total_members > 0 ? ((data.active_members / data.total_members) * 100).toFixed(2) : 0;
    const churnRate = data.total_members > 0 ? ((data.lost_members / data.total_members) * 100).toFixed(2) : 0;
    
    res.json({
      success: true,
      data: {
        totalMembers: data.total_members,
        activeMembers: data.active_members,
        newMembers: data.new_members,
        lostMembers: data.lost_members,
        activeRate: parseFloat(activeRate),
        churnRate: parseFloat(churnRate)
      },
      month: month
    });
  } catch (error) {
    console.error('获取会员概览数据失败:', error);
    res.status(500).json({
      success: false,
      message: '获取数据失败',
      error: error.message
    });
  }
});

// 健康检查接口
app.get('/health', (req, res) => {
  res.json({ status: 'OK', timestamp: new Date().toISOString() });
});

// 启动服务器
app.listen(PORT, () => {
  console.log(`CRFM-E API服务器运行在端口 ${PORT}`);
  console.log(`健康检查: http://localhost:${PORT}/health`);
  console.log(`CRFM-E分布数据: http://localhost:${PORT}/api/crfme/distribution`);
  console.log(`CRFM-E条形图数据: http://localhost:${PORT}/api/crfme/bar-chart`);
});

// 优雅关闭
process.on('SIGINT', async () => {
  console.log('正在关闭服务器...');
  await pool.end();
  process.exit(0);
});