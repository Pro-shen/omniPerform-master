package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * MOT管理控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/mot")
@Api(tags = "MOT管理")
public class MotController {

    private static final Logger log = LoggerFactory.getLogger(MotController.class);

    /**
     * 获取MOT任务列表
     */
    @GetMapping("/tasks")
    @ApiOperation("获取MOT任务列表")
    public Result getMotTasks(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String priority) {
        try {
            List<Map<String, Object>> tasks = new ArrayList<>();
            
            String[] types = {"首购后回访", "购买后指导", "复购提醒", "生日关怀", "节日问候", "产品推荐", "使用指导", "满意度调研"};
            String[] priorities = {"高", "中", "低"};
            String[] statuses = {"待执行", "执行中", "已完成", "已取消"};
            String[] guides = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
            
            for (int i = 0; i < 20; i++) {
                Map<String, Object> task = new HashMap<>();
                task.put("id", i + 1);
                task.put("memberId", "M" + String.format("%06d", i + 1));
                task.put("memberName", "会员" + (i + 1));
                task.put("motType", types[i % types.length]);
                task.put("priority", priorities[i % priorities.length]);
                task.put("status", statuses[i % statuses.length]);
                task.put("guideName", guides[i % guides.length]);
                task.put("createDate", LocalDate.now().minusDays(i % 7).toString());
                task.put("dueDate", LocalDate.now().plusDays((i % 3) + 1).toString());
                task.put("description", "针对" + task.get("memberName") + "的" + task.get("motType"));
                tasks.add(task);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("tasks", tasks.subList((page - 1) * size, Math.min(page * size, tasks.size())));
            result.put("total", tasks.size());
            result.put("page", page);
            result.put("size", size);
            
            log.info("获取MOT任务列表成功，页码: {}, 大小: {}", page, size);
            return Result.success("获取MOT任务列表成功", result);
        } catch (Exception e) {
            log.error("获取MOT任务列表失败: {}", e.getMessage(), e);
            return Result.error("获取MOT任务列表失败");
        }
    }

    /**
     * 创建MOT任务
     */
    @PostMapping("/tasks")
    @ApiOperation("创建MOT任务")
    public Result createMotTask(@RequestBody Map<String, Object> taskData) {
        try {
            Map<String, Object> task = new HashMap<>();
            task.put("id", System.currentTimeMillis());
            task.put("memberId", taskData.get("memberId"));
            task.put("memberName", taskData.get("memberName"));
            task.put("motType", taskData.get("motType"));
            task.put("priority", taskData.get("priority"));
            task.put("status", "待执行");
            task.put("guideName", taskData.get("guideName"));
            task.put("createDate", LocalDate.now().toString());
            task.put("dueDate", taskData.get("dueDate"));
            task.put("description", taskData.get("description"));
            
            log.info("创建MOT任务成功，任务ID: {}", task.get("id"));
            return Result.success("创建MOT任务成功", task);
        } catch (Exception e) {
            log.error("创建MOT任务失败: {}", e.getMessage(), e);
            return Result.error("创建MOT任务失败");
        }
    }

    /**
     * 更新MOT任务
     */
    @PutMapping("/tasks/{taskId}")
    @ApiOperation("更新MOT任务")
    public Result updateMotTask(@PathVariable String taskId, @RequestBody Map<String, Object> taskData) {
        try {
            Map<String, Object> task = new HashMap<>();
            task.put("id", taskId);
            task.put("memberId", taskData.get("memberId"));
            task.put("memberName", taskData.get("memberName"));
            task.put("motType", taskData.get("motType"));
            task.put("priority", taskData.get("priority"));
            task.put("status", taskData.get("status"));
            task.put("guideName", taskData.get("guideName"));
            task.put("dueDate", taskData.get("dueDate"));
            task.put("description", taskData.get("description"));
            task.put("updateDate", LocalDate.now().toString());
            
            log.info("更新MOT任务成功，任务ID: {}", taskId);
            return Result.success("更新MOT任务成功", task);
        } catch (Exception e) {
            log.error("更新MOT任务失败: {}", e.getMessage(), e);
            return Result.error("更新MOT任务失败");
        }
    }

    /**
     * 删除MOT任务
     */
    @DeleteMapping("/tasks/{taskId}")
    @ApiOperation("删除MOT任务")
    public Result deleteMotTask(@PathVariable String taskId) {
        try {
            log.info("删除MOT任务成功，任务ID: {}", taskId);
            return Result.success("删除MOT任务成功");
        } catch (Exception e) {
            log.error("删除MOT任务失败: {}", e.getMessage(), e);
            return Result.error("删除MOT任务失败");
        }
    }

    /**
     * 获取MOT任务详情
     */
    @GetMapping("/tasks/{taskId}")
    @ApiOperation("获取MOT任务详情")
    public Result getMotTaskDetail(@PathVariable String taskId) {
        try {
            Map<String, Object> task = new HashMap<>();
            task.put("id", taskId);
            task.put("memberId", "M000001");
            task.put("memberName", "张三");
            task.put("motType", "首购后回访");
            task.put("priority", "高");
            task.put("status", "待执行");
            task.put("guideName", "李四");
            task.put("createDate", LocalDate.now().toString());
            task.put("dueDate", LocalDate.now().plusDays(1).toString());
            task.put("description", "针对新会员张三的首购后回访");
            
            // 执行记录
            List<Map<String, Object>> executionHistory = new ArrayList<>();
            Map<String, Object> record = new HashMap<>();
            record.put("date", LocalDate.now().toString());
            record.put("action", "任务创建");
            record.put("operator", "系统");
            record.put("note", "自动创建MOT任务");
            executionHistory.add(record);
            task.put("executionHistory", executionHistory);
            
            log.info("获取MOT任务详情成功，任务ID: {}", taskId);
            return Result.success("获取MOT任务详情成功", task);
        } catch (Exception e) {
            log.error("获取MOT任务详情失败: {}", e.getMessage(), e);
            return Result.error("获取MOT任务详情失败");
        }
    }

    /**
     * 执行MOT任务
     */
    @PostMapping("/tasks/{taskId}/execute")
    @ApiOperation("执行MOT任务")
    public Result executeMotTask(@PathVariable String taskId, @RequestBody Map<String, Object> executionData) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("status", "已完成");
            result.put("executionDate", LocalDate.now().toString());
            result.put("executor", executionData.get("executor"));
            result.put("result", executionData.get("result"));
            result.put("note", executionData.get("note"));
            
            log.info("执行MOT任务成功，任务ID: {}", taskId);
            return Result.success("执行MOT任务成功", result);
        } catch (Exception e) {
            log.error("执行MOT任务失败: {}", e.getMessage(), e);
            return Result.error("执行MOT任务失败");
        }
    }

    /**
     * 获取MOT统计数据
     */
    @GetMapping("/statistics")
    @ApiOperation("获取MOT统计数据")
    public Result getMotStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 任务状态统计
            Map<String, Integer> statusStats = new HashMap<>();
            statusStats.put("待执行", 156);
            statusStats.put("执行中", 89);
            statusStats.put("已完成", 1234);
            statusStats.put("已取消", 23);
            statistics.put("statusStats", statusStats);
            
            // 任务类型统计
            Map<String, Integer> typeStats = new HashMap<>();
            typeStats.put("首购后回访", 234);
            typeStats.put("购买后指导", 189);
            typeStats.put("复购提醒", 156);
            typeStats.put("生日关怀", 123);
            typeStats.put("节日问候", 98);
            statistics.put("typeStats", typeStats);
            
            // 完成率统计
            statistics.put("completionRate", 82.5);
            statistics.put("onTimeRate", 78.3);
            statistics.put("satisfactionRate", 4.6);
            
            log.info("获取MOT统计数据成功");
            return Result.success("获取MOT统计数据成功", statistics);
        } catch (Exception e) {
            log.error("获取MOT统计数据失败: {}", e.getMessage(), e);
            return Result.error("获取MOT统计数据失败");
        }
    }
}