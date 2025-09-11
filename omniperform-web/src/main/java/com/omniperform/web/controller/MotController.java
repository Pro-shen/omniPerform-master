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
@CrossOrigin(origins = "*")
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
            String[] guides = {"张小丽", "李明华", "王雅琪", "赵晓燕", "钱志强", "孙美玲", "周建国", "吴佳慧"};
            String[] memberNames = {"李雅婷", "王晓敏", "张美琳", "陈思雨", "刘佳怡", "赵雪莹", "孙梦洁", "周欣怡", "吴雨桐", "郑晓雯", 
                                   "黄诗涵", "徐梦琪", "朱雅静", "林思妤", "何雨萱", "罗美娜", "高雅琳", "梁诗雅", "谢雨欣", "韩美丽",
                                   "冯雅芳", "曹思琪", "彭雨婷", "蒋美玉", "魏雅娟", "董思颖", "薛雨洁", "范美华", "邓雅丽", "石思敏"};
            
            for (int i = 0; i < 20; i++) {
                Map<String, Object> task = new HashMap<>();
                task.put("id", i + 1);
                task.put("memberId", "M" + String.format("%06d", i + 1));
                task.put("memberName", memberNames[i % memberNames.length]);
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
            task.put("memberName", "李雅婷");
            task.put("motType", "首购后回访");
            task.put("priority", "高");
            task.put("status", "待执行");
            task.put("guideName", "张小丽");
            task.put("createDate", LocalDate.now().toString());
            task.put("dueDate", LocalDate.now().plusDays(1).toString());
            task.put("description", "针对新会员李雅婷的首购后回访");
            
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

    /**
     * 获取MOT任务分布数据
     */
    @GetMapping("/distribution")
    @ApiOperation("获取MOT任务分布数据")
    public Result getMotDistribution() {
        try {
            Map<String, Object> distribution = new HashMap<>();
            
            // 按类型分布
            List<Map<String, Object>> typeDistribution = new ArrayList<>();
            String[] types = {"首购后回访", "购买后指导", "复购提醒", "生日关怀", "节日问候"};
            int[] typeCounts = {234, 189, 156, 123, 98};
            
            for (int i = 0; i < types.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", types[i]);
                item.put("value", typeCounts[i]);
                typeDistribution.add(item);
            }
            distribution.put("typeDistribution", typeDistribution);
            
            // 按状态分布
            List<Map<String, Object>> statusDistribution = new ArrayList<>();
            String[] statuses = {"待执行", "执行中", "已完成", "已取消"};
            int[] statusCounts = {156, 89, 1234, 23};
            
            for (int i = 0; i < statuses.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", statuses[i]);
                item.put("value", statusCounts[i]);
                statusDistribution.add(item);
            }
            distribution.put("statusDistribution", statusDistribution);
            
            log.info("获取MOT任务分布数据成功");
            return Result.success("获取MOT任务分布数据成功", distribution);
        } catch (Exception e) {
            log.error("获取MOT任务分布数据失败: {}", e.getMessage(), e);
            return Result.error("获取MOT任务分布数据失败");
        }
    }

    /**
     * 获取MOT执行效率趋势数据
     */
    @GetMapping("/efficiency-trend")
    @ApiOperation("获取MOT执行效率趋势数据")
    public Result getEfficiencyTrend(@RequestParam(defaultValue = "7") int days) {
        try {
            Map<String, Object> trend = new HashMap<>();
            
            List<String> dates = new ArrayList<>();
            List<Double> completionRates = new ArrayList<>();
            List<Double> onTimeRates = new ArrayList<>();
            List<Double> satisfactionRates = new ArrayList<>();
            
            for (int i = days - 1; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                dates.add(date.toString());
                
                // 模拟数据波动
                completionRates.add(75.0 + Math.random() * 20);
                onTimeRates.add(70.0 + Math.random() * 25);
                satisfactionRates.add(4.0 + Math.random() * 1);
            }
            
            trend.put("dates", dates);
            trend.put("completionRates", completionRates);
            trend.put("onTimeRates", onTimeRates);
            trend.put("satisfactionRates", satisfactionRates);
            
            log.info("获取MOT执行效率趋势数据成功，天数: {}", days);
            return Result.success("获取MOT执行效率趋势数据成功", trend);
        } catch (Exception e) {
            log.error("获取MOT执行效率趋势数据失败: {}", e.getMessage(), e);
            return Result.error("获取MOT执行效率趋势数据失败");
        }
    }

    /**
     * 获取MOT类型话术数据
     */
    @GetMapping("/scripts")
    @ApiOperation("获取MOT类型话术数据")
    public Result getMotScripts() {
        try {
            List<Map<String, Object>> scripts = new ArrayList<>();
            
            // 首购后回访
            Map<String, Object> script1 = new HashMap<>();
            script1.put("id", "first-purchase");
            script1.put("type", "首购后回访");
            script1.put("title", "首购后3天回访");
            script1.put("trigger", "首次购买后3天");
            script1.put("target", "确认产品使用情况，解答初次使用问题，增强会员信任感。");
            script1.put("scripts", new String[]{
                "【称呼】您好，我是您在XX门店购买婴幼儿奶粉的导购XX。感谢您对我们产品的信任！想跟您确认一下，宝宝这几天使用我们的奶粉情况如何？有没有遇到什么问题需要解答的？",
                "【如果使用顺利】非常高兴听到宝宝适应良好！我们的产品采用先进的水解配方，特别适合宝宝的肠胃吸收。如果您有任何关于冲调或保存的问题，随时可以咨询我。",
                "【如果有问题】非常感谢您的反馈。关于您提到的问题，我建议您可以尝试...（针对性解答）。如果问题仍然存在，我可以为您联系我们的专业育婴顾问进行一对一指导。"
            });
            script1.put("notes", new String[]{
                "必须在任务生成后24小时内完成首次联系",
                "记录宝宝使用反馈，标记是否有过敏或不适情况",
                "如有异常情况，及时上报主管并安排专业顾问跟进"
            });
            script1.put("duration", "5-10分钟");
            script1.put("badgeClass", "bg-primary");
            script1.put("cardClass", "");
            scripts.add(script1);
            
            // 购买后指导
            Map<String, Object> script2 = new HashMap<>();
            script2.put("id", "guidance");
            script2.put("type", "购买后指导");
            script2.put("title", "购买后15天指导");
            script2.put("trigger", "购买后15天");
            script2.put("target", "提供专业喂养指导，解决使用过程中的问题，增强产品价值感。");
            script2.put("scripts", new String[]{
                "【称呼】您好，我是您在XX门店的专属导购XX。宝宝使用我们的奶粉已经半个月了，想跟您分享一些专业的喂养小技巧，帮助宝宝获得更好的营养吸收。",
                "【根据宝宝月龄】您的宝宝现在X个月了，这个阶段宝宝的营养需求主要是...（提供针对性建议）",
                "【水解配方特点】我们的水解配方特别适合宝宝消化吸收，建议您在冲调时注意水温控制在40-50度，这样可以最大程度保留活性营养成分...",
                "【收集反馈】请问宝宝这段时间的精神状态、睡眠和大便情况如何？有没有需要我帮您解答的问题？"
            });
            script2.put("notes", new String[]{
                "根据宝宝月龄提供个性化的喂养建议",
                "重点强调水解配方的科学价值和正确使用方法",
                "记录宝宝成长数据，为后续沟通做准备"
            });
            script2.put("duration", "10-15分钟");
            script2.put("badgeClass", "bg-info");
            script2.put("cardClass", "success");
            scripts.add(script2);
            
            // 复购提醒
            Map<String, Object> script3 = new HashMap<>();
            script3.put("id", "repurchase");
            script3.put("type", "复购提醒");
            script3.put("title", "购买后25天复购提醒");
            script3.put("trigger", "购买后25天");
            script3.put("target", "提醒会员及时补充奶粉，避免断货，促进复购转化。");
            script3.put("scripts", new String[]{
                "【称呼】您好，我是您的专属导购XX。根据您上次购买的奶粉用量，预计最近几天就要用完了，特地提前和您确认一下，是否需要为您准备新的奶粉？",
                "【如果需要】太好了，我已经为您预留了新鲜库存。您方便什么时候来门店取货？如果您时间紧张，我们也提供配送服务。",
                "【如果犹豫】目前我们有限时的会员专属活动，复购可享受XX优惠。另外，保持同一品牌奶粉对宝宝肠道健康非常重要，避免频繁更换带来的适应问题。",
                "【如果拒绝】感谢您的反馈。请问是对我们的产品有什么不满意的地方吗？（记录原因）我们非常重视您的体验，希望有机会继续为您和宝宝提供服务。"
            });
            script3.put("notes", new String[]{
                "提前了解会员可能享受的优惠活动",
                "准备好应对各种异议的话术",
                "记录拒绝原因，为产品和服务改进提供依据"
            });
            script3.put("duration", "5-10分钟");
            script3.put("badgeClass", "bg-warning");
            script3.put("cardClass", "warning");
            scripts.add(script3);
            
            // 生日关怀
            Map<String, Object> script4 = new HashMap<>();
            script4.put("id", "birthday");
            script4.put("type", "生日关怀");
            script4.put("title", "生日关怀");
            script4.put("trigger", "宝宝/妈妈生日前3天");
            script4.put("target", "通过生日关怀增强情感连接，提升会员忠诚度。");
            script4.put("scripts", new String[]{
                "【宝宝生日】【称呼】您好！我是您的专属导购XX。马上就是宝宝的生日了，提前送上我们最真挚的祝福！宝宝在这一年里有了哪些成长和变化呢？",
                "我们为宝宝准备了专属的生日礼物，包含成长纪念册和营养礼包，欢迎您在方便的时候到门店领取。同时，我们也为宝宝新的一年准备了专属的营养方案...",
                "【妈妈生日】【称呼】您好！我是您的专属导购XX。得知明天是您的生日，特别想送上我们的祝福！作为宝宝健康成长的守护者，您辛苦了！",
                "我们为您准备了一份专属的生日礼物，表达我们的感谢与祝福。您方便什么时候来门店领取呢？"
            });
            script4.put("notes", new String[]{
                "区分宝宝生日和妈妈生日，使用不同的话术",
                "提前确认生日礼品准备情况",
                "记录会员反馈，更新会员画像"
            });
            script4.put("duration", "5分钟");
            script4.put("badgeClass", "bg-danger");
            script4.put("cardClass", "danger");
            scripts.add(script4);
            
            // 节日问候
            Map<String, Object> script5 = new HashMap<>();
            script5.put("id", "festival");
            script5.put("type", "节日问候");
            script5.put("title", "节日问候");
            script5.put("trigger", "重要节日前3天");
            script5.put("target", "通过节日问候维系会员关系，创造营销机会。");
            script5.put("scripts", new String[]{
                "【春节】【称呼】您好！农历新年即将到来，我代表XX品牌向您和宝宝送上新春的祝福！祝宝宝在新的一年里健康成长，聪明可爱！",
                "我们准备了新春限定礼盒，包含奶粉和精美玩具，欢迎您来门店选购，为宝宝的新年增添一份健康礼物。",
                "【儿童节】【称呼】您好！六一儿童节即将到来，祝宝宝节日快乐！童年是人生中最美好的时光，我们希望能陪伴宝宝健康快乐地成长。",
                "我们特别策划了儿童节亲子活动，包含营养知识讲座和互动游戏，诚挚邀请您和宝宝参加。同时，我们也为会员准备了节日专属优惠..."
            });
            script5.put("notes", new String[]{
                "根据不同节日准备相应的话术和活动",
                "结合节日特点，设计有吸引力的营销方案",
                "注意节日问候的时间点，不宜过早或过晚"
            });
            script5.put("duration", "5分钟");
            script5.put("badgeClass", "bg-success");
            script5.put("cardClass", "xiaohongshu");
            scripts.add(script5);
            
            log.info("获取MOT类型话术数据成功");
            return Result.success("获取MOT类型话术数据成功", scripts);
        } catch (Exception e) {
            log.error("获取MOT类型话术数据失败: {}", e.getMessage(), e);
            return Result.error("获取MOT类型话术数据失败");
        }
    }

    /**
     * 获取今日MOT任务概览
     */
    @GetMapping("/today-overview")
    @ApiOperation("获取今日MOT任务概览")
    public Result getTodayOverview() {
        try {
            Map<String, Object> overview = new HashMap<>();
            
            // 今日任务统计
            overview.put("totalTasks", 156);
            overview.put("completedTasks", 89);
            overview.put("pendingTasks", 67);
            overview.put("completionRate", 57.1);
            
            // MOT发送率
            overview.put("sendRate", 92.3);
            overview.put("sendRateTrend", "+2.1%");
            
            // 沟通率
            overview.put("communicationRate", 78.5);
            overview.put("communicationRateTrend", "+1.8%");
            
            // 成功率
            overview.put("successRate", 65.2);
            overview.put("successRateTrend", "+3.2%");
            
            log.info("获取今日MOT任务概览成功");
            return Result.success("获取今日MOT任务概览成功", overview);
        } catch (Exception e) {
            log.error("获取今日MOT任务概览失败: {}", e.getMessage(), e);
            return Result.error("获取今日MOT任务概览失败");
        }
    }
}