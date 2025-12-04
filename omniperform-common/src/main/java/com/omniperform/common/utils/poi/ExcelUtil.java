package com.omniperform.common.utils.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.annotation.Excel.Type;
import com.omniperform.common.annotation.Excels;
import com.omniperform.common.core.domain.AjaxResult;
import com.omniperform.common.core.text.Convert;
import com.omniperform.common.exception.UtilException;
import com.omniperform.common.utils.DateUtils;
import com.omniperform.common.utils.DictUtils;
import com.omniperform.common.utils.StringUtils;
import com.omniperform.common.utils.file.FileTypeUtils;
import com.omniperform.common.utils.file.FileUtils;
import com.omniperform.common.utils.file.ImageUtils;
import com.omniperform.common.utils.reflect.ReflectUtils;

/**
 * Excel相关处理
 * 
 * @author omniperform
 */
public class ExcelUtil<T>
{
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static final String SEPARATOR = ",";

    public static final String FORMULA_REGEX_STR = "=|-|\\+|@";

    public static final String[] FORMULA_STR = { "=", "-", "+", "@" };

    /**
     * 用于dictType属性数据存储，避免重复查缓存
     */
    public Map<String, String> sysDictMap = new HashMap<String, String>();

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Type type;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 当前行号
     */
    private int rownum;

    /**
     * 标题
     */
    private String title;

    /**
     * 最大高度
     */
    private short maxHeight;

    /**
     * 合并后最后行数
     */
    private int subMergedLastRowNum = 0;

    /**
     * 合并后开始行数
     */
    private int subMergedFirstRowNum = 1;

    /**
     * 对象的子列表方法
     */
    private Method subMethod;

    /**
     * 对象的子列表属性
     */
    private List<Field> subFields;

    /**
     * 统计列表
     */
    private Map<Integer, Double> statistics = new HashMap<Integer, Double>();

    /**
     * 数字格式
     */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    /**
     * 实体对象
     */
    public Class<T> clazz;

    /**
     * 需要显示列属性
     */
    public String[] includeFields;

    /**
     * 需要排除列属性
     */
    public String[] excludeFields;

    public ExcelUtil(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    /**
     * 验证Excel模板格式
     * 
     * @param clazz 实体类
     * @param sheet 工作表
     * @return 验证结果
     */
    public ExcelValidationResult validateExcelTemplate(Class<T> clazz, Sheet sheet)
    {
        ExcelValidationResult result = new ExcelValidationResult();
        
        try 
        {
            // 获取表头行 - 假设第一行是表头
            int headerRowIndex = 0;
            Row headerRow = sheet.getRow(headerRowIndex);
            if (headerRow == null)
            {
                result.addError("未找到表头行，请检查Excel文件格式");
                log.error("Excel模板验证 - 未找到表头行: headerRowIndex={}", headerRowIndex);
                return result;
            }
            
            // 获取实体类字段信息
            List<Object[]> fields = this.getFields();
            Map<String, String> requiredHeaders = new HashMap<>();
            Map<String, Boolean> fieldRequiredMap = new HashMap<>();
            
            for (Object[] os : fields)
            {
                Excel attr = (Excel) os[1];
                String headerName = attr.name();
                requiredHeaders.put(headerName, ((Field) os[0]).getName());
                fieldRequiredMap.put(headerName, false); // Excel注解中没有isRequired方法，默认为false
                log.debug("Excel模板验证 - 期望表头: {}, 字段: {}, 必填: {}", 
                        headerName, ((Field) os[0]).getName(), false);
            }
            
            // 验证表头
            List<String> actualHeaders = new ArrayList<>();
            List<String> missingHeaders = new ArrayList<>();
            List<String> extraHeaders = new ArrayList<>();
            
            // 读取实际表头
            for (int i = 0; i < headerRow.getLastCellNum(); i++)
            {
                Cell cell = headerRow.getCell(i);
                String headerValue = getCellValue(headerRow, i).toString().trim();
                if (StringUtils.isNotEmpty(headerValue))
                {
                    actualHeaders.add(headerValue);
                    log.debug("Excel模板验证 - 实际表头[{}]: {}", i, headerValue);
                }
            }
            
            // 检查缺失的必填表头
            for (Map.Entry<String, Boolean> entry : fieldRequiredMap.entrySet())
            {
                String requiredHeader = entry.getKey();
                Boolean isRequired = entry.getValue();
                
                if (!actualHeaders.contains(requiredHeader))
                {
                    if (isRequired)
                    {
                        missingHeaders.add(requiredHeader);
                        result.addError("缺少必填表头: " + requiredHeader);
                        log.error("Excel模板验证 - 缺少必填表头: {}", requiredHeader);
                    }
                    else
                    {
                        log.warn("Excel模板验证 - 缺少可选表头: {}", requiredHeader);
                    }
                }
            }
            
            // 检查多余的表头
            for (String actualHeader : actualHeaders)
            {
                if (!requiredHeaders.containsKey(actualHeader))
                {
                    extraHeaders.add(actualHeader);
                    result.addWarning("发现未定义的表头: " + actualHeader);
                    log.warn("Excel模板验证 - 未定义的表头: {}", actualHeader);
                }
            }
            
            // 检查表头顺序建议
            List<String> expectedOrder = new ArrayList<>(requiredHeaders.keySet());
            if (!actualHeaders.equals(expectedOrder))
            {
                result.addWarning("表头顺序与建议顺序不一致，建议顺序: " + String.join(", ", expectedOrder));
                log.warn("Excel模板验证 - 表头顺序不一致，期望: {}, 实际: {}", expectedOrder, actualHeaders);
            }
            
            // 设置验证结果统计信息
            result.setTotalExpectedHeaders(requiredHeaders.size());
            result.setTotalActualHeaders(actualHeaders.size());
            result.setMissingHeaders(missingHeaders);
            result.setExtraHeaders(extraHeaders);
            
            log.info("Excel模板验证完成 - 期望表头数: {}, 实际表头数: {}, 缺失: {}, 多余: {}, 错误数: {}, 警告数: {}", 
                    requiredHeaders.size(), actualHeaders.size(), missingHeaders.size(), 
                    extraHeaders.size(), result.getErrors().size(), result.getWarnings().size());
        }
        catch (Exception e)
        {
            result.addError("Excel模板验证异常: " + e.getMessage());
            log.error("Excel模板验证异常", e);
        }
        
        return result;
    }

    /**
     * 验证Excel数据行格式
     * 
     * @param sheet 工作表
     * @param maxRows 最大检查行数，-1表示检查所有行
     * @return 验证结果
     */
    public ExcelValidationResult validateExcelData(Sheet sheet, int maxRows)
    {
        ExcelValidationResult result = new ExcelValidationResult();
        
        try 
        {
            int headerRowIndex = 0; // 假设第一行是表头
            int lastRowNum = sheet.getLastRowNum();
            int checkRows = maxRows > 0 ? Math.min(lastRowNum, headerRowIndex + maxRows) : lastRowNum;
            
            log.info("Excel数据验证开始 - 总行数: {}, 检查行数: {}", lastRowNum, checkRows - headerRowIndex);
            
            for (int i = headerRowIndex + 1; i <= checkRows; i++)
            {
                Row row = sheet.getRow(i);
                if (row == null)
                {
                    result.addWarning("第" + (i + 1) + "行为空行");
                    continue;
                }
                
                // 检查行是否完全为空
                if (isRowEmpty(row))
                {
                    result.addWarning("第" + (i + 1) + "行为空行");
                    continue;
                }
                
                // 检查每个单元格的数据格式
                validateRowData(row, result);
            }
            
            log.info("Excel数据验证完成 - 检查了{}行数据，错误数: {}, 警告数: {}", 
                    checkRows - headerRowIndex, result.getErrors().size(), result.getWarnings().size());
        }
        catch (Exception e)
        {
            result.addError("Excel数据验证异常: " + e.getMessage());
            log.error("Excel数据验证异常", e);
        }
        
        return result;
    }

    /**
     * 验证单行数据
     * 
     * @param row 数据行
     * @param result 验证结果
     */
    private void validateRowData(Row row, ExcelValidationResult result)
    {
        try 
        {
            List<Object[]> fields = this.getFields();
            
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel attr = (Excel) os[1];
                int columnIndex = 0; // 简化处理，使用默认列索引
                
                if (columnIndex >= 0)
                {
                    Cell cell = row.getCell(columnIndex);
                    Object cellValue = getCellValue(row, columnIndex);
                    
                    // 检查必填字段 - 默认为非必填
                    boolean isRequired = false; // Excel注解没有isRequired方法，默认为false
                    if (isRequired && (cellValue == null || "".equals(cellValue.toString().trim())))
                    {
                        result.addError("第" + (row.getRowNum() + 1) + "行，列'" + attr.name() + "'为必填项，不能为空");
                        continue;
                    }
                    
                    // 检查数据类型兼容性
                    if (cellValue != null && !"".equals(cellValue.toString().trim()))
                    {
                        validateDataType(row.getRowNum() + 1, attr.name(), cellValue, field.getType(), result);
                    }
                }
            }
        }
        catch (Exception e)
        {
            result.addError("第" + (row.getRowNum() + 1) + "行数据验证异常: " + e.getMessage());
            log.error("行数据验证异常 - 行号: {}", row.getRowNum() + 1, e);
        }
    }

    /**
     * 验证数据类型兼容性
     * 
     * @param rowNum 行号
     * @param fieldName 字段名
     * @param value 值
     * @param targetType 目标类型
     * @param result 验证结果
     */
    private void validateDataType(int rowNum, String fieldName, Object value, Class<?> targetType, ExcelValidationResult result)
    {
        try 
        {
            String strValue = value.toString().trim();
            
            if (Integer.TYPE == targetType || Integer.class == targetType)
            {
                if (!StringUtils.isNumeric(strValue))
                {
                    result.addError("第" + rowNum + "行，列'" + fieldName + "'应为整数，实际值: " + strValue);
                }
            }
            else if (Long.TYPE == targetType || Long.class == targetType)
            {
                if (!StringUtils.isNumeric(strValue))
                {
                    result.addError("第" + rowNum + "行，列'" + fieldName + "'应为长整数，实际值: " + strValue);
                }
            }
            else if (Double.TYPE == targetType || Double.class == targetType || 
                     Float.TYPE == targetType || Float.class == targetType)
            {
                try 
                {
                    Double.parseDouble(strValue);
                }
                catch (NumberFormatException e)
                {
                    result.addError("第" + rowNum + "行，列'" + fieldName + "'应为数字，实际值: " + strValue);
                }
            }
            else if (BigDecimal.class == targetType)
            {
                try 
                {
                    new BigDecimal(strValue);
                }
                catch (NumberFormatException e)
                {
                    result.addError("第" + rowNum + "行，列'" + fieldName + "'应为数字，实际值: " + strValue);
                }
            }
            else if (Date.class == targetType)
            {
                // 日期验证相对复杂，这里做基本检查
                if (!(value instanceof Date) && !isValidDateString(strValue))
                {
                    result.addWarning("第" + rowNum + "行，列'" + fieldName + "'可能不是有效的日期格式: " + strValue);
                }
            }
            else if (Boolean.TYPE == targetType || Boolean.class == targetType)
            {
                String lowerValue = strValue.toLowerCase();
                if (!("true".equals(lowerValue) || "false".equals(lowerValue) || 
                      "1".equals(strValue) || "0".equals(strValue) ||
                      "是".equals(strValue) || "否".equals(strValue)))
                {
                    result.addWarning("第" + rowNum + "行，列'" + fieldName + "'应为布尔值，实际值: " + strValue);
                }
            }
        }
        catch (Exception e)
        {
            log.warn("数据类型验证异常 - 行: {}, 字段: {}, 值: {}", rowNum, fieldName, value, e);
        }
    }

    /**
     * 检查字符串是否为有效日期格式
     * 
     * @param dateStr 日期字符串
     * @return 是否有效
     */
    private boolean isValidDateString(String dateStr)
    {
        if (StringUtils.isEmpty(dateStr))
        {
            return false;
        }
        
        // 常见日期格式
        String[] dateFormats = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd",
            "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "MM/dd/yyyy", "dd/MM/yyyy", "dd-MM-yyyy"
        };
        
        for (String format : dateFormats)
        {
            try 
            {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                sdf.parse(dateStr);
                return true;
            }
            catch (ParseException e)
            {
                // 继续尝试下一个格式
            }
        }
        
        return false;
    }

    /**
     * Excel验证结果类
     */
    public static class ExcelValidationResult
    {
        private List<String> errors = new ArrayList<>();
        private List<String> warnings = new ArrayList<>();
        private int totalExpectedHeaders = 0;
        private int totalActualHeaders = 0;
        private List<String> missingHeaders = new ArrayList<>();
        private List<String> extraHeaders = new ArrayList<>();
        
        public void addError(String error)
        {
            errors.add(error);
        }
        
        public void addWarning(String warning)
        {
            warnings.add(warning);
        }
        
        public boolean isValid()
        {
            return errors.isEmpty();
        }
        
        public boolean hasWarnings()
        {
            return !warnings.isEmpty();
        }
        
        // Getters and Setters
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
        public int getTotalExpectedHeaders() { return totalExpectedHeaders; }
        public void setTotalExpectedHeaders(int totalExpectedHeaders) { this.totalExpectedHeaders = totalExpectedHeaders; }
        public int getTotalActualHeaders() { return totalActualHeaders; }
        public void setTotalActualHeaders(int totalActualHeaders) { this.totalActualHeaders = totalActualHeaders; }
        public List<String> getMissingHeaders() { return missingHeaders; }
        public void setMissingHeaders(List<String> missingHeaders) { this.missingHeaders = missingHeaders; }
        public List<String> getExtraHeaders() { return extraHeaders; }
        public void setExtraHeaders(List<String> extraHeaders) { this.extraHeaders = extraHeaders; }
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Excel验证结果:\n");
            sb.append("- 有效: ").append(isValid()).append("\n");
            sb.append("- 错误数: ").append(errors.size()).append("\n");
            sb.append("- 警告数: ").append(warnings.size()).append("\n");
            
            if (!errors.isEmpty())
            {
                sb.append("错误详情:\n");
                for (String error : errors)
                {
                    sb.append("  - ").append(error).append("\n");
                }
            }
            
            if (!warnings.isEmpty())
            {
                sb.append("警告详情:\n");
                for (String warning : warnings)
                {
                    sb.append("  - ").append(warning).append("\n");
                }
            }
            
            return sb.toString();
        }
    }

    /**
     * 仅在Excel中显示列属性
     *
     * @param fields 列属性名 示例[单个"name"/多个"id","name"]
     */
    public void showColumn(String... fields)
    {
        this.includeFields = fields;
    }

    /**
     * 隐藏Excel中列属性
     *
     * @param fields 列属性名 示例[单个"name"/多个"id","name"]
     */
    public void hideColumn(String... fields)
    {
        this.excludeFields = fields;
    }

    public void init(List<T> list, String sheetName, String title, Type type)
    {
        if (list == null)
        {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        this.title = title;
        createExcelField();
        createWorkbook();
        createTitle();
        createSubHead();
    }

    /**
     * 创建excel第一行标题
     */
    public void createTitle()
    {
        if (StringUtils.isNotEmpty(title))
        {
            int titleLastCol = this.fields.size() - 1;
            if (isSubList())
            {
                titleLastCol = titleLastCol + subFields.size() - 1;
            }
            Row titleRow = sheet.createRow(rownum == 0 ? rownum++ : 0);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, titleLastCol));
        }
    }

    /**
     * 创建对象的子列表名称
     */
    public void createSubHead()
    {
        if (isSubList())
        {
            Row subRow = sheet.createRow(rownum);
            int column = 0;
            int subFieldSize = subFields != null ? subFields.size() : 0;
            for (Object[] objects : fields)
            {
                Field field = (Field) objects[0];
                Excel attr = (Excel) objects[1];
                if (Collection.class.isAssignableFrom(field.getType()))
                {
                    Cell cell = subRow.createCell(column);
                    cell.setCellValue(attr.name());
                    cell.setCellStyle(styles.get(StringUtils.format("header_{}_{}", attr.headerColor(), attr.headerBackgroundColor())));
                    if (subFieldSize > 1)
                    {
                        CellRangeAddress cellAddress = new CellRangeAddress(rownum, rownum, column, column + subFieldSize - 1);
                        sheet.addMergedRegion(cellAddress);
                    }
                    column += subFieldSize;
                }
                else
                {
                    Cell cell = subRow.createCell(column++);
                    cell.setCellValue(attr.name());
                    cell.setCellStyle(styles.get(StringUtils.format("header_{}_{}", attr.headerColor(), attr.headerBackgroundColor())));
                }
            }
            rownum++;
        }
    }

    /**
     * 对excel表单默认第一个索引名转换成list
     * 
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(InputStream is)
    {
        return importExcel(is, 0);
    }

    /**
     * 对excel表单默认第一个索引名转换成list
     * 
     * @param is 输入流
     * @param titleNum 标题占用行数
     * @return 转换后集合
     */
    public List<T> importExcel(InputStream is, int titleNum)
    {
        List<T> list = null;
        try
        {
            list = importExcel(StringUtils.EMPTY, is, titleNum);
        }
        catch (Exception e)
        {
            log.error("导入Excel异常{}", e.getMessage());
            throw new UtilException(e.getMessage());
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
        return list;
    }

    /**
     * 对excel表单指定表格索引名转换成list
     * 
     * @param sheetName 表格索引名
     * @param titleNum 标题占用行数
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(String sheetName, InputStream is, int titleNum) throws Exception
    {
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        // 如果指定sheet名,则取指定sheet中的内容 否则默认指向第1个sheet
        Sheet sheet = StringUtils.isNotEmpty(sheetName) ? wb.getSheet(sheetName) : wb.getSheetAt(0);
        if (sheet == null)
        {
            throw new IOException("文件sheet不存在");
        }
        boolean isXSSFWorkbook = !(wb instanceof HSSFWorkbook);
        Map<String, List<PictureData>> pictures = null;
        if (isXSSFWorkbook)
        {
            pictures = getSheetPictures07((XSSFSheet) sheet, (XSSFWorkbook) wb);
        }
        else
        {
            pictures = getSheetPictures03((HSSFSheet) sheet, (HSSFWorkbook) wb);
        }
        // 获取最后一个非空行的行下标，比如总行数为n，则返回的为n-1
        int rows = sheet.getLastRowNum();
        if (rows > 0)
        {
            // 定义一个map用于存放excel列的序号和field.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // 获取表头
            Row heard = sheet.getRow(titleNum);
            if (heard == null) {
                log.error("Excel解析 - 表头行为空: 行号={}", titleNum);
                throw new RuntimeException("Excel文件表头行为空，行号: " + titleNum);
            }
            
            log.info("Excel解析 - 表头行数据: 行号={}, 物理列数={}, 最后列号={}", 
                titleNum, heard.getPhysicalNumberOfCells(), heard.getLastCellNum());
            
            // 打印表头行的所有单元格内容
            StringBuilder headerContent = new StringBuilder("Excel解析 - 表头行完整内容: [");
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                Object cellValue = this.getCellValue(heard, i);
                headerContent.append("列").append(i).append("='").append(cellValue).append("'");
                if (i < heard.getPhysicalNumberOfCells() - 1) {
                    headerContent.append(", ");
                }
            }
            headerContent.append("]");
            log.info(headerContent.toString());
            
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                if (StringUtils.isNotNull(cell))
                {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                    log.info("Excel解析 - 表头映射: 列{}='{}' -> 索引{}", i, value, i);
                }
                else
                {
                    cellMap.put(null, i);
                    log.warn("Excel解析 - 表头为空: 列{}", i);
                }
            }
            log.info("Excel解析 - 表头映射完成: cellMap大小={}, 内容={}", cellMap.size(), cellMap);
            // 有数据时才处理 得到类的所有field.
            List<Object[]> fields = this.getFields();
            log.info("Excel解析 - 实体类字段数量: {}", fields.size());
            
            // 打印所有@Excel注解的字段信息
            log.info("Excel解析 - 实体类@Excel字段详情:");
            for (Object[] objects : fields)
            {
                Field field = (Field) objects[0];
                Excel attr = (Excel) objects[1];
                log.info("  字段名: {}, @Excel(name='{}')", field.getName(), attr.name());
            }
            
            Map<Integer, Object[]> fieldsMap = new HashMap<Integer, Object[]>();
            List<String> unmatchedFields = new ArrayList<>();
            
            for (Object[] objects : fields)
            {
                Excel attr = (Excel) objects[1];
                Integer column = cellMap.get(attr.name());
                if (column != null)
                {
                    fieldsMap.put(column, objects);
                    log.info("Excel解析 - 字段映射成功: @Excel(name='{}') -> 列索引{} -> 字段'{}'", 
                        attr.name(), column, ((Field) objects[0]).getName());
                }
                else
                {
                    unmatchedFields.add(attr.name());
                    log.warn("Excel解析 - 字段映射失败: @Excel(name='{}') 在表头中未找到对应列", attr.name());
                }
            }
            
            log.info("Excel解析 - 字段映射完成: fieldsMap大小={}", fieldsMap.size());
            if (!unmatchedFields.isEmpty()) {
                log.error("Excel解析 - 未匹配的字段列表: {}", unmatchedFields);
                log.error("Excel解析 - 可用的表头列表: {}", cellMap.keySet());
                
                // 进行模糊匹配分析
                log.info("Excel解析 - 开始模糊匹配分析:");
                for (String unmatchedField : unmatchedFields) {
                    for (String headerKey : cellMap.keySet()) {
                        if (headerKey != null && (headerKey.contains(unmatchedField) || unmatchedField.contains(headerKey))) {
                            log.info("  可能匹配: '{}' <-> '{}'", unmatchedField, headerKey);
                        }
                    }
                }
            }
            for (int i = titleNum + 1; i <= rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                log.info("Excel解析 - 处理数据行: 行号={}", i);
                // 判断当前行是否是空行
                if (isRowEmpty(row))
                {
                    log.info("Excel解析 - 跳过空行: 行号={}", i);
                    continue;
                }
                T entity = null;
                for (Map.Entry<Integer, Object[]> entry : fieldsMap.entrySet())
                {
                    Object val = this.getCellValue(row, entry.getKey());
                    log.debug("Excel解析 - 读取单元格值: 行{}列{} = '{}'", i, entry.getKey(), val);

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = (Field) entry.getValue()[0];
                    Excel attr = (Excel) entry.getValue()[1];
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    
                    Object originalVal = val; // 保存原始值用于错误日志
                    
                    try 
                    {
                        // 数据类型转换
                        val = convertFieldValue(val, fieldType, field, attr, i, entry.getKey());
                        
                        if (StringUtils.isNotNull(fieldType))
                        {
                            String propertyName = field.getName();
                            if (StringUtils.isNotEmpty(attr.targetAttr()))
                            {
                                propertyName = field.getName() + "." + attr.targetAttr();
                            }
                            
                            // 应用转换器和字典映射
                            val = applyConvertersAndDictionary(val, attr, row, entry.getKey());
                            
                            // 设置字段值
                            try 
                            {
                                ReflectUtils.invokeSetter(entity, propertyName, val);
                                log.debug("Excel解析 - 字段赋值成功: 字段'{}' = '{}'", propertyName, val);
                            }
                            catch (Exception setterException)
                            {
                                log.error("Excel解析 - 字段赋值失败: 行={}, 列={}, 字段='{}', 值='{}', 类型={}, 异常={}", 
                                        i, entry.getKey(), propertyName, val, fieldType.getSimpleName(), setterException.getMessage());
                                // 尝试设置为null或默认值
                                try 
                                {
                                    ReflectUtils.invokeSetter(entity, propertyName, getDefaultValue(fieldType));
                                    log.info("Excel解析 - 字段赋值回退成功: 字段'{}' 设置为默认值", propertyName);
                                }
                                catch (Exception fallbackException)
                                {
                                    log.warn("Excel解析 - 字段赋值回退也失败: 字段='{}', 回退异常={}", 
                                            propertyName, fallbackException.getMessage());
                                }
                            }
                        }
                    }
                    catch (Exception conversionException)
                    {
                        log.error("Excel解析 - 数据转换失败: 行={}, 列={}, 字段='{}', 原值='{}', 目标类型={}, 异常={}", 
                                i, entry.getKey(), field.getName(), originalVal, fieldType.getSimpleName(), conversionException.getMessage());
                        // 转换失败时跳过该字段，继续处理其他字段
                    }
                }
                if (entity != null) {
                    log.info("Excel解析 - 添加实体到列表: 行号={}, 实体={}", i, entity);
                }
                list.add(entity);
            }
        }
        log.info("Excel解析 - 解析完成: 总共解析{}条数据", list.size());
        return list;
    }

    /**
     * 增强的Excel导入方法，支持智能表头识别和模糊匹配
     * 
     * @param is 输入流
     * @param titleNum 表头行号（从0开始）
     * @return 导入结果包装类
     */
    public ExcelImportResult<T> importExcelEnhanced(InputStream is, int titleNum) {
        ExcelImportResult<T> result = new ExcelImportResult<>();
        try {
            this.type = Type.IMPORT;
            this.wb = WorkbookFactory.create(is);
            List<T> list = new ArrayList<T>();
            
            // 如果指定sheet名,则取指定sheet中的内容 否则默认指向第1个sheet
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                result.addError("Excel文件sheet不存在");
                return result;
            }
            
            // 获取最后一个非空行的行下标
            int rows = sheet.getLastRowNum();
            if (rows <= titleNum) {
                result.addError("Excel文件数据行不足，至少需要" + (titleNum + 2) + "行（包含表头和数据行）");
                return result;
            }
            
            // 智能表头识别
            HeaderMatchResult headerResult = smartHeaderMatching(sheet, titleNum);
            if (!headerResult.isSuccess()) {
                result.addError("表头识别失败：" + headerResult.getErrorMessage());
                result.addWarning("建议使用标准模板，确保表头与字段名称匹配");
                return result;
            }
            
            Map<Integer, Object[]> fieldsMap = headerResult.getFieldsMap();
            result.addInfo("成功识别" + fieldsMap.size() + "个字段映射");
            
            // 数据行处理
            for (int i = titleNum + 1; i <= rows; i++) {
                Row row = sheet.getRow(i);
                if (isRowEmpty(row)) {
                    result.addWarning("第" + (i + 1) + "行为空行，已跳过");
                    continue;
                }
                
                try {
                    T entity = parseRowToEntity(row, fieldsMap, i);
                    if (entity != null) {
                        list.add(entity);
                        result.incrementSuccessCount();
                    } else {
                        result.addError("第" + (i + 1) + "行数据解析失败：所有字段都为空或无效");
                        result.incrementFailCount();
                    }
                } catch (Exception e) {
                    result.addError("第" + (i + 1) + "行数据解析异常：" + e.getMessage());
                    result.incrementFailCount();
                    log.error("Excel解析 - 第{}行数据解析异常", i + 1, e);
                }
            }
            
            result.setData(list);
            result.addInfo("Excel导入完成，成功：" + result.getSuccessCount() + "条，失败：" + result.getFailCount() + "条");
            
        } catch (Exception e) {
            result.addError("Excel文件解析失败：" + e.getMessage());
            log.error("Excel解析异常", e);
        }
        
        return result;
    }
    
    /**
     * 智能表头匹配
     */
    private HeaderMatchResult smartHeaderMatching(Sheet sheet, int titleNum) {
        HeaderMatchResult result = new HeaderMatchResult();
        
        try {
            // 获取表头行
            Row headerRow = sheet.getRow(titleNum);
            if (headerRow == null) {
                result.setErrorMessage("表头行（第" + (titleNum + 1) + "行）为空");
                return result;
            }
            
            // 读取表头内容
            Map<String, Integer> cellMap = new HashMap<>();
            List<String> actualHeaders = new ArrayList<>();
            
            for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String value = getCellValue(headerRow, i).toString().trim();
                    if (!value.isEmpty()) {
                        cellMap.put(value, i);
                        actualHeaders.add(value);
                    }
                }
            }
            
            log.info("Excel解析 - 实际表头: {}", actualHeaders);
            
            // 获取实体类字段
            List<Object[]> fields = this.getFields();
            Map<Integer, Object[]> fieldsMap = new HashMap<>();
            List<String> unmatchedFields = new ArrayList<>();
            List<String> matchedFields = new ArrayList<>();
            
            // 精确匹配
            for (Object[] objects : fields) {
                Excel attr = (Excel) objects[1];
                String expectedHeader = attr.name();
                
                Integer column = cellMap.get(expectedHeader);
                if (column != null) {
                    fieldsMap.put(column, objects);
                    matchedFields.add(expectedHeader);
                    log.info("Excel解析 - 精确匹配: '{}' -> 列{}", expectedHeader, column);
                } else {
                    unmatchedFields.add(expectedHeader);
                }
            }
            
            // 模糊匹配未匹配的字段
            if (!unmatchedFields.isEmpty()) {
                log.info("Excel解析 - 开始模糊匹配，未匹配字段: {}", unmatchedFields);
                
                for (String unmatchedField : new ArrayList<>(unmatchedFields)) {
                    String bestMatch = findBestHeaderMatch(unmatchedField, actualHeaders, matchedFields);
                    if (bestMatch != null) {
                        Integer column = cellMap.get(bestMatch);
                        if (column != null) {
                            // 找到对应的字段对象
                            for (Object[] objects : fields) {
                                Excel attr = (Excel) objects[1];
                                if (attr.name().equals(unmatchedField)) {
                                    fieldsMap.put(column, objects);
                                    matchedFields.add(bestMatch);
                                    unmatchedFields.remove(unmatchedField);
                                    log.info("Excel解析 - 模糊匹配: '{}' -> '{}' -> 列{}", unmatchedField, bestMatch, column);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            result.setFieldsMap(fieldsMap);
            result.setMatchedHeaders(matchedFields);
            result.setUnmatchedFields(unmatchedFields);
            result.setActualHeaders(actualHeaders);
            
            if (fieldsMap.isEmpty()) {
                result.setErrorMessage("没有找到任何匹配的字段，请检查表头是否正确");
                return result;
            }
            
            result.setSuccess(true);
            log.info("Excel解析 - 表头匹配完成: 成功匹配{}个字段，未匹配{}个字段", 
                    fieldsMap.size(), unmatchedFields.size());
            
        } catch (Exception e) {
            result.setErrorMessage("表头匹配异常：" + e.getMessage());
            log.error("Excel解析 - 表头匹配异常", e);
        }
        
        return result;
    }
    
    /**
     * 寻找最佳表头匹配
     */
    private String findBestHeaderMatch(String expectedHeader, List<String> actualHeaders, List<String> excludeHeaders) {
        String bestMatch = null;
        int maxScore = 0;
        
        for (String actualHeader : actualHeaders) {
            if (excludeHeaders.contains(actualHeader)) {
                continue; // 跳过已经匹配的表头
            }
            
            int score = calculateSimilarityScore(expectedHeader, actualHeader);
            if (score > maxScore && score >= 60) { // 相似度阈值60%
                maxScore = score;
                bestMatch = actualHeader;
            }
        }
        
        return bestMatch;
    }
    
    /**
     * 计算字符串相似度得分（0-100）
     */
    private int calculateSimilarityScore(String str1, String str2) {
        if (str1 == null || str2 == null) return 0;
        if (str1.equals(str2)) return 100;
        
        // 包含关系检查
        if (str1.contains(str2) || str2.contains(str1)) {
            return 80;
        }
        
        // 去除空格后比较
        String clean1 = str1.replaceAll("\\s+", "");
        String clean2 = str2.replaceAll("\\s+", "");
        if (clean1.equals(clean2)) {
            return 90;
        }
        
        // 简单的编辑距离算法
        int distance = levenshteinDistance(clean1, clean2);
        int maxLen = Math.max(clean1.length(), clean2.length());
        if (maxLen == 0) return 100;
        
        return Math.max(0, 100 - (distance * 100 / maxLen));
    }
    
    /**
     * 计算编辑距离
     */
    private int levenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        
        for (int i = 0; i <= str1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= str2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[str1.length()][str2.length()];
    }
    
    /**
     * 解析行数据为实体对象
     */
    private T parseRowToEntity(Row row, Map<Integer, Object[]> fieldsMap, int rowIndex) throws Exception {
        T entity = clazz.newInstance();
        boolean hasValidData = false;
        
        for (Map.Entry<Integer, Object[]> entry : fieldsMap.entrySet()) {
            try {
                Object val = getCellValue(row, entry.getKey());
                
                Field field = (Field) entry.getValue()[0];
                Excel attr = (Excel) entry.getValue()[1];
                Class<?> fieldType = field.getType();
                
                // 使用增强的数据类型转换
                val = convertFieldValueEnhanced(val, fieldType, field, attr, rowIndex, entry.getKey());
                
                if (val != null && !val.toString().trim().isEmpty()) {
                    hasValidData = true;
                }
                
                // 设置字段值
                String propertyName = field.getName();
                if (StringUtils.isNotEmpty(attr.targetAttr())) {
                    propertyName = field.getName() + "." + attr.targetAttr();
                }
                
                // 应用转换器和字典映射
                val = applyConvertersAndDictionary(val, attr, row, entry.getKey());
                
                ReflectUtils.invokeSetter(entity, propertyName, val);
                
            } catch (Exception e) {
                log.warn("Excel解析 - 第{}行第{}列数据处理失败: {}", rowIndex + 1, entry.getKey() + 1, e.getMessage());
                // 继续处理其他字段，不中断整行解析
            }
        }
        
        return hasValidData ? entity : null;
    }
    
    /**
     * Excel导入结果包装类
     */
    public static class ExcelImportResult<T> {
        private List<T> data = new ArrayList<>();
        private List<String> errors = new ArrayList<>();
        private List<String> warnings = new ArrayList<>();
        private List<String> infos = new ArrayList<>();
        private int successCount = 0;
        private int failCount = 0;
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public void addWarning(String warning) {
            warnings.add(warning);
        }
        
        public void addInfo(String info) {
            infos.add(info);
        }
        
        public void incrementSuccessCount() {
            successCount++;
        }
        
        public void incrementFailCount() {
            failCount++;
        }
        
        public boolean isSuccess() {
            return errors.isEmpty() && !data.isEmpty();
        }
        
        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }
        
        // Getters and setters
        public List<T> getData() { return data; }
        public void setData(List<T> data) { this.data = data; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }
        public List<String> getInfos() { return infos; }
        public int getSuccessCount() { return successCount; }
        public int getFailCount() { return failCount; }
        public int getTotalCount() { return successCount + failCount; }
    }
    
    /**
     * 表头匹配结果类
     */
    private static class HeaderMatchResult {
        private boolean success = false;
        private String errorMessage;
        private Map<Integer, Object[]> fieldsMap = new HashMap<>();
        private List<String> matchedHeaders = new ArrayList<>();
        private List<String> unmatchedFields = new ArrayList<>();
        private List<String> actualHeaders = new ArrayList<>();
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public Map<Integer, Object[]> getFieldsMap() { return fieldsMap; }
        public void setFieldsMap(Map<Integer, Object[]> fieldsMap) { this.fieldsMap = fieldsMap; }
        public List<String> getMatchedHeaders() { return matchedHeaders; }
        public void setMatchedHeaders(List<String> matchedHeaders) { this.matchedHeaders = matchedHeaders; }
        public List<String> getUnmatchedFields() { return unmatchedFields; }
        public void setUnmatchedFields(List<String> unmatchedFields) { this.unmatchedFields = unmatchedFields; }
        public List<String> getActualHeaders() { return actualHeaders; }
        public void setActualHeaders(List<String> actualHeaders) { this.actualHeaders = actualHeaders; }
    }

    /**
     * 转换字段值 - 增强版本，支持更多数据类型
     */
    private Object convertFieldValueEnhanced(Object val, Class<?> fieldType, Field field, Excel attr, int rowIndex, int columnIndex) {
        if (val == null || val.toString().trim().isEmpty()) {
            return null;
        }
        
        try {
            String strVal = val.toString().trim();
            
            // 日期类型转换
            if (fieldType == Date.class) {
                if (val instanceof Date) {
                    return val;
                }
                return parseDateSafe(strVal, attr);
            }
            
            // 字符串类型
            if (fieldType == String.class) {
                if (val instanceof Date) {
                    // 如果是日期对象且目标是字符串，尝试按注解格式格式化
                    String dateFormat = attr.dateFormat();
                    if (StringUtils.isEmpty(dateFormat)) {
                        dateFormat = "yyyy-MM-dd HH:mm:ss";
                    }
                    return new SimpleDateFormat(dateFormat).format((Date) val);
                }
                return strVal;
            }
            
            // 数值类型转换
            if (fieldType == Integer.class || fieldType == int.class) {
                return parseIntegerSafe(strVal);
            }
            
            if (fieldType == Long.class || fieldType == long.class) {
                return parseLongSafe(strVal);
            }
            
            if (fieldType == Double.class || fieldType == double.class) {
                return parseDoubleSafe(strVal);
            }
            
            if (fieldType == BigDecimal.class) {
                return new BigDecimal(strVal);
            }
            
            // 布尔类型转换
            if (fieldType == Boolean.class || fieldType == boolean.class) {
                return parseBooleanSafe(strVal);
            }
            
            return strVal;
            
        } catch (Exception e) {
            log.warn("Excel解析 - 第{}行第{}列数据类型转换失败: {} -> {}, 错误: {}", 
                    rowIndex + 1, columnIndex + 1, val, fieldType.getSimpleName(), e.getMessage());
            return null;
        }
    }
    
    /**
     * 安全解析整数
     */
    private Integer parseIntegerSafe(String value) {
        if (StringUtils.isEmpty(value)) return null;
        
        try {
            // 处理小数点（Excel数值可能带小数点）
            if (value.contains(".")) {
                return (int) Double.parseDouble(value);
            }
            
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("整数解析失败: {}", value);
            return null;
        }
    }
    
    /**
     * 安全解析长整数
     */
    private Long parseLongSafe(String value) {
        if (StringUtils.isEmpty(value)) return null;
        
        try {
            // 处理小数点
            if (value.contains(".")) {
                return (long) Double.parseDouble(value);
            }
            
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("长整数解析失败: {}", value);
            return null;
        }
    }
    
    /**
     * 安全解析双精度浮点数
     */
    private Double parseDoubleSafe(String value) {
        if (StringUtils.isEmpty(value)) return null;
        
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("浮点数解析失败: {}", value);
            return null;
        }
    }
    
    /**
     * 安全解析日期
     */
    private Date parseDateSafe(String value, Excel attr) {
        if (StringUtils.isEmpty(value)) return null;
        
        try {
            // 使用注解中指定的日期格式
            String dateFormat = attr.dateFormat();
            if (StringUtils.isNotEmpty(dateFormat)) {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                return sdf.parse(value);
            }
            
            // 尝试常见的日期格式
            String[] formats = {
                "yyyy-MM-dd",
                "yyyy/MM/dd", 
                "yyyy-MM-dd HH:mm:ss",
                "yyyy/MM/dd HH:mm:ss",
                "yyyyMMdd",
                "yyyy年MM月dd日"
            };
            
            for (String format : formats) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    return sdf.parse(value);
                } catch (ParseException ignored) {
                    // 继续尝试下一个格式
                }
            }
            
            // 如果都失败了，尝试解析为时间戳
            return new Date(Long.parseLong(value));
            
        } catch (Exception e) {
            log.warn("日期解析失败: {}", value);
            return null;
        }
    }
    
    /**
     * 安全解析布尔值
     */
    private Boolean parseBooleanSafe(String value) {
        if (StringUtils.isEmpty(value)) return null;
        
        String lowerValue = value.toLowerCase();
        return "true".equals(lowerValue) || "1".equals(value) || 
               "是".equals(value) || "yes".equals(lowerValue) || "y".equals(lowerValue);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public AjaxResult exportExcel(List<T> list, String sheetName)
    {
        return exportExcel(list, sheetName, StringUtils.EMPTY);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @param title 标题
     * @return 结果
     */
    public AjaxResult exportExcel(List<T> list, String sheetName, String title)
    {
        this.init(list, sheetName, title, Type.EXPORT);
        return exportExcel();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param response 返回数据
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String sheetName)
    {
        exportExcel(response, list, sheetName, StringUtils.EMPTY);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param response 返回数据
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @param title 标题
     * @return 结果
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String sheetName, String title)
    {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        this.init(list, sheetName, title, Type.EXPORT);
        exportExcel(response);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public AjaxResult importTemplateExcel(String sheetName)
    {
        return importTemplateExcel(sheetName, StringUtils.EMPTY);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param sheetName 工作表的名称
     * @param title 标题
     * @return 结果
     */
    public AjaxResult importTemplateExcel(String sheetName, String title)
    {
        this.init(null, sheetName, title, Type.IMPORT);
        return exportExcel();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public void importTemplateExcel(HttpServletResponse response, String sheetName)
    {
        importTemplateExcel(response, sheetName, StringUtils.EMPTY);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @param sheetName 工作表的名称
     * @param title 标题
     * @return 结果
     */
    public void importTemplateExcel(HttpServletResponse response, String sheetName, String title)
    {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        this.init(null, sheetName, title, Type.IMPORT);
        exportExcel(response);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @return 结果
     */
    public void exportExcel(HttpServletResponse response)
    {
        try
        {
            writeSheet();
            wb.write(response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
        }
        finally
        {
            IOUtils.closeQuietly(wb);
        }
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 
     * @return 结果
     */
    public AjaxResult exportExcel()
    {
        OutputStream out = null;
        try
        {
            writeSheet();
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
            throw new UtilException("导出Excel失败，请联系网站管理员！");
        }
        finally
        {
            IOUtils.closeQuietly(wb);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 创建写入数据到Sheet
     */
    public void writeSheet()
    {
        // 取出一共有多少个sheet.
        int sheetNo = Math.max(1, (int) Math.ceil(list.size() * 1.0 / sheetSize));
        for (int index = 0; index < sheetNo; index++)
        {
            createSheet(sheetNo, index);

            // 产生一行
            Row row = sheet.createRow(rownum);
            int column = 0;
            // 写入各个字段的列头名称
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                if (Collection.class.isAssignableFrom(field.getType()))
                {
                    for (Field subField : subFields)
                    {
                        Excel subExcel = subField.getAnnotation(Excel.class);
                        this.createHeadCell(subExcel, row, column++);
                    }
                }
                else
                {
                    this.createHeadCell(excel, row, column++);
                }
            }
            if (Type.EXPORT.equals(type))
            {
                fillExcelData(index, row);
                addStatisticsRow();
            }
        }
    }

    /**
     * 填充excel数据
     * 
     * @param index 序号
     * @param row 单元格行
     */
    @SuppressWarnings("unchecked")
    public void fillExcelData(int index, Row row)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        int currentRowNum = rownum + 1; // 从标题行后开始

        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(currentRowNum);
            T vo = (T) list.get(i);
            int column = 0;
            int maxSubListSize = getCurrentMaxSubListSize(vo);
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                if (Collection.class.isAssignableFrom(field.getType()))
                {
                    try
                    {
                        Collection<?> subList = (Collection<?>) getTargetValue(vo, field, excel);
                        if (subList != null && !subList.isEmpty())
                        {
                            int subIndex = 0;
                            for (Object subVo : subList)
                            {
                                Row subRow = sheet.getRow(currentRowNum + subIndex);
                                if (subRow == null)
                                {
                                    subRow = sheet.createRow(currentRowNum + subIndex);
                                }

                                int subColumn = column;
                                for (Field subField : subFields)
                                {
                                    Excel subExcel = subField.getAnnotation(Excel.class);
                                    addCell(subExcel, subRow, (T) subVo, subField, subColumn++);
                                }
                                subIndex++;
                            }
                            column += subFields.size();
                        }
                    }
                    catch (Exception e)
                    {
                        log.error("填充集合数据失败", e);
                    }
                }
                else
                {
                    // 创建单元格并设置值
                    addCell(excel, row, vo, field, column);
                    if (maxSubListSize > 1 && excel.needMerge())
                    {
                        sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + maxSubListSize - 1, column, column));
                    }
                    column++;
                }
            }
            currentRowNum += maxSubListSize;
        }
    }

    /**
     * 获取子列表最大数
     */
    private int getCurrentMaxSubListSize(T vo)
    {
        int maxSubListSize = 1;
        for (Object[] os : fields)
        {
            Field field = (Field) os[0];
            if (Collection.class.isAssignableFrom(field.getType()))
            {
                try
                {
                    Collection<?> subList = (Collection<?>) getTargetValue(vo, field, (Excel) os[1]);
                    if (subList != null && !subList.isEmpty())
                    {
                        maxSubListSize = Math.max(maxSubListSize, subList.size());
                    }
                }
                catch (Exception e)
                {
                    log.error("获取集合大小失败", e);
                }
            }
        }
        return maxSubListSize;
    }

    /**
     * 创建表格样式
     * 
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb)
    {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        DataFormat dataFormat = wb.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("@"));
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font totalFont = wb.createFont();
        totalFont.setFontName("Arial");
        totalFont.setFontHeightInPoints((short) 10);
        style.setFont(totalFont);
        styles.put("total", style);

        styles.putAll(annotationHeaderStyles(wb, styles));

        styles.putAll(annotationDataStyles(wb));

        return styles;
    }

    /**
     * 根据Excel注解创建表格头样式
     * 
     * @param wb 工作薄对象
     * @return 自定义样式列表
     */
    private Map<String, CellStyle> annotationHeaderStyles(Workbook wb, Map<String, CellStyle> styles)
    {
        Map<String, CellStyle> headerStyles = new HashMap<String, CellStyle>();
        for (Object[] os : fields)
        {
            Excel excel = (Excel) os[1];
            String key = StringUtils.format("header_{}_{}", excel.headerColor(), excel.headerBackgroundColor());
            if (!headerStyles.containsKey(key))
            {
                CellStyle style = wb.createCellStyle();
                style.cloneStyleFrom(styles.get("data"));
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setFillForegroundColor(excel.headerBackgroundColor().index);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font headerFont = wb.createFont();
                headerFont.setFontName("Arial");
                headerFont.setFontHeightInPoints((short) 10);
                headerFont.setBold(true);
                headerFont.setColor(excel.headerColor().index);
                style.setFont(headerFont);
                // 设置表格头单元格文本形式
                DataFormat dataFormat = wb.createDataFormat();
                style.setDataFormat(dataFormat.getFormat("@"));
                headerStyles.put(key, style);
            }
        }
        return headerStyles;
    }

    /**
     * 根据Excel注解创建表格列样式
     * 
     * @param wb 工作薄对象
     * @return 自定义样式列表
     */
    private Map<String, CellStyle> annotationDataStyles(Workbook wb)
    {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        for (Object[] os : fields)
        {
            Field field = (Field) os[0];
            Excel excel = (Excel) os[1];
            if (Collection.class.isAssignableFrom(field.getType()))
            {
                ParameterizedType pt = (ParameterizedType) field.getGenericType();
                Class<?> subClass = (Class<?>) pt.getActualTypeArguments()[0];
                List<Field> subFields = FieldUtils.getFieldsListWithAnnotation(subClass, Excel.class);
                for (Field subField : subFields)
                {
                    Excel subExcel = subField.getAnnotation(Excel.class);
                    annotationDataStyles(styles, subField, subExcel);
                }
            }
            else
            {
                annotationDataStyles(styles, field, excel);
            }
        }
        return styles;
    }

    /**
     * 根据Excel注解创建表格列样式
     * 
     * @param styles 自定义样式列表
     * @param field  属性列信息
     * @param excel  注解信息
     */
    public void annotationDataStyles(Map<String, CellStyle> styles, Field field, Excel excel)
    {
        String key = StringUtils.format("data_{}_{}_{}_{}_{}", excel.align(), excel.color(), excel.backgroundColor(), excel.cellType(), excel.wrapText());
        if (!styles.containsKey(key))
        {
            CellStyle style = wb.createCellStyle();
            style.setAlignment(excel.align());
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(excel.backgroundColor().getIndex());
            style.setWrapText(excel.wrapText());
            Font dataFont = wb.createFont();
            dataFont.setFontName("Arial");
            dataFont.setFontHeightInPoints((short) 10);
            dataFont.setColor(excel.color().index);
            style.setFont(dataFont);
            if (ColumnType.TEXT == excel.cellType())
            {
                DataFormat dataFormat = wb.createDataFormat();
                style.setDataFormat(dataFormat.getFormat("@"));
            }
            styles.put(key, style);
        }
    }

    /**
     * 创建单元格
     */
    public Cell createHeadCell(Excel attr, Row row, int column)
    {
        // 创建列
        Cell cell = row.createCell(column);
        // 写入列信息
        cell.setCellValue(attr.name());
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get(StringUtils.format("header_{}_{}", attr.headerColor(), attr.headerBackgroundColor())));
        if (isSubList())
        {
            // 填充默认样式，防止合并单元格样式失效
            sheet.setDefaultColumnStyle(column, styles.get(StringUtils.format("data_{}_{}_{}_{}_{}", attr.align(), attr.color(), attr.backgroundColor(), attr.cellType(), attr.wrapText())));
            if (attr.needMerge())
            {
                sheet.addMergedRegion(new CellRangeAddress(rownum - 1, rownum, column, column));
            }
        }
        return cell;
    }

    /**
     * 设置单元格信息
     * 
     * @param value 单元格值
     * @param attr 注解相关
     * @param cell 单元格信息
     */
    public void setCellVo(Object value, Excel attr, Cell cell)
    {
        if (ColumnType.STRING == attr.cellType() || ColumnType.TEXT == attr.cellType())
        {
            String cellValue = Convert.toStr(value);
            // 对于任何以表达式触发字符 =-+@开头的单元格，直接使用tab字符作为前缀，防止CSV注入。
            if (StringUtils.startsWithAny(cellValue, FORMULA_STR))
            {
                cellValue = RegExUtils.replaceFirst(cellValue, FORMULA_REGEX_STR, "\t$0");
            }
            if (value instanceof Collection && StringUtils.equals("[]", cellValue))
            {
                cellValue = StringUtils.EMPTY;
            }
            cell.setCellValue(StringUtils.isNull(cellValue) ? attr.defaultValue() : cellValue + attr.suffix());
        }
        else if (ColumnType.NUMERIC == attr.cellType())
        {
            if (StringUtils.isNotNull(value))
            {
                cell.setCellValue(StringUtils.contains(Convert.toStr(value), ".") ? Convert.toDouble(value) : Convert.toInt(value));
            }
        }
        else if (ColumnType.IMAGE == attr.cellType())
        {
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) cell.getColumnIndex(), cell.getRow().getRowNum(), (short) (cell.getColumnIndex() + 1), cell.getRow().getRowNum() + 1);
            String propertyValue = Convert.toStr(value);
            if (StringUtils.isNotEmpty(propertyValue))
            {
                List<String> imagePaths = StringUtils.str2List(propertyValue, SEPARATOR);
                for (String imagePath : imagePaths)
                {
                    byte[] data = ImageUtils.getImage(imagePath);
                    getDrawingPatriarch(cell.getSheet()).createPicture(anchor, cell.getSheet().getWorkbook().addPicture(data, getImageType(data)));
                }
            }
        }
    }

    /**
     * 获取画布
     */
    public static Drawing<?> getDrawingPatriarch(Sheet sheet)
    {
        if (sheet.getDrawingPatriarch() == null)
        {
            sheet.createDrawingPatriarch();
        }
        return sheet.getDrawingPatriarch();
    }

    /**
     * 获取图片类型,设置图片插入类型
     */
    public int getImageType(byte[] value)
    {
        String type = FileTypeUtils.getFileExtendName(value);
        if ("JPG".equalsIgnoreCase(type))
        {
            return Workbook.PICTURE_TYPE_JPEG;
        }
        else if ("PNG".equalsIgnoreCase(type))
        {
            return Workbook.PICTURE_TYPE_PNG;
        }
        return Workbook.PICTURE_TYPE_JPEG;
    }

    /**
     * 创建表格样式
     */
    public void setDataValidation(Excel attr, Row row, int column)
    {
        if (attr.name().indexOf("注：") >= 0)
        {
            sheet.setColumnWidth(column, 6000);
        }
        else
        {
            // 设置列宽
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
        }
        if (StringUtils.isNotEmpty(attr.prompt()) || attr.combo().length > 0 || attr.comboReadDict())
        {
            String[] comboArray = attr.combo();
            if (attr.comboReadDict())
            {
                if (!sysDictMap.containsKey("combo_" + attr.dictType()))
                {
                    String labels = DictUtils.getDictLabels(attr.dictType());
                    sysDictMap.put("combo_" + attr.dictType(), labels);
                }
                String val = sysDictMap.get("combo_" + attr.dictType());
                comboArray = StringUtils.split(val, DictUtils.SEPARATOR);
            }
            if (comboArray.length > 15 || StringUtils.join(comboArray).length() > 255)
            {
                // 如果下拉数大于15或字符串长度大于255，则使用一个新sheet存储，避免生成的模板下拉值获取不到
                setXSSFValidationWithHidden(sheet, comboArray, attr.prompt(), 1, 100, column, column);
            }
            else
            {
                // 提示信息或只能选择不能输入的列内容.
                setPromptOrValidation(sheet, comboArray, attr.prompt(), 1, 100, column, column);
            }
        }
    }

    /**
     * 添加单元格
     */
    public Cell addCell(Excel attr, Row row, T vo, Field field, int column)
    {
        Cell cell = null;
        try
        {
            // 设置行高
            row.setHeight(maxHeight);
            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
            if (attr.isExport())
            {
                // 创建cell
                cell = row.createCell(column);
                if (isSubListValue(vo) && getListCellValue(vo).size() > 1 && attr.needMerge())
                {
                    if (subMergedLastRowNum >= subMergedFirstRowNum)
                    {
                        sheet.addMergedRegion(new CellRangeAddress(subMergedFirstRowNum, subMergedLastRowNum, column, column));
                    }
                }
                cell.setCellStyle(styles.get(StringUtils.format("data_{}_{}_{}_{}_{}", attr.align(), attr.color(), attr.backgroundColor(), attr.cellType(), attr.wrapText())));

                // 用于读取对象中的属性
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                String separator = attr.separator();
                String dictType = attr.dictType();
                if (StringUtils.isNotEmpty(dateFormat) && StringUtils.isNotNull(value))
                {
                    cell.getCellStyle().setDataFormat(this.wb.getCreationHelper().createDataFormat().getFormat(dateFormat));
                    cell.setCellValue(parseDateToStr(dateFormat, value));
                }
                else if (StringUtils.isNotEmpty(readConverterExp) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(convertByExp(Convert.toStr(value), readConverterExp, separator));
                }
                else if (StringUtils.isNotEmpty(dictType) && StringUtils.isNotNull(value))
                {
                    if (!sysDictMap.containsKey(dictType + value))
                    {
                        String lable = convertDictByExp(Convert.toStr(value), dictType, separator);
                        sysDictMap.put(dictType + value, lable);
                    }
                    cell.setCellValue(sysDictMap.get(dictType + value));
                }
                else if (value instanceof BigDecimal && -1 != attr.scale())
                {
                    cell.setCellValue((((BigDecimal) value).setScale(attr.scale(), attr.roundingMode())).doubleValue());
                }
                else if (!attr.handler().equals(ExcelHandlerAdapter.class))
                {
                    cell.setCellValue(dataFormatHandlerAdapter(value, attr, cell));
                }
                else
                {
                    // 设置列类型
                    setCellVo(value, attr, cell);
                }
                addStatisticsData(column, Convert.toStr(value), attr);
            }
        }
        catch (Exception e)
        {
            log.error("导出Excel失败{}", e);
        }
        return cell;
    }

    /**
     * 设置 POI XSSFSheet 单元格提示或选择框
     * 
     * @param sheet 表单
     * @param textlist 下拉框显示的内容
     * @param promptContent 提示内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     */
    public void setPromptOrValidation(Sheet sheet, String[] textlist, String promptContent, int firstRow, int endRow,
            int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = textlist.length > 0 ? helper.createExplicitListConstraint(textlist) : helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        if (StringUtils.isNotEmpty(promptContent))
        {
            // 如果设置了提示信息则鼠标放上去提示
            dataValidation.createPromptBox("", promptContent);
            dataValidation.setShowPromptBox(true);
        }
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation)
        {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        else
        {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框（兼容超出一定数量的下拉框）.
     * 
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param promptContent 提示内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     */
    public void setXSSFValidationWithHidden(Sheet sheet, String[] textlist, String promptContent, int firstRow, int endRow, int firstCol, int endCol)
    {
        String hideSheetName = "combo_" + firstCol + "_" + endCol;
        Sheet hideSheet = wb.createSheet(hideSheetName); // 用于存储 下拉菜单数据
        for (int i = 0; i < textlist.length; i++)
        {
            hideSheet.createRow(i).createCell(0).setCellValue(textlist[i]);
        }
        // 创建名称，可被其他单元格引用
        Name name = wb.createName();
        name.setNameName(hideSheetName + "_data");
        name.setRefersToFormula(hideSheetName + "!$A$1:$A$" + textlist.length);
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createFormulaListConstraint(hideSheetName + "_data");
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        if (StringUtils.isNotEmpty(promptContent))
        {
            // 如果设置了提示信息则鼠标放上去提示
            dataValidation.createPromptBox("", promptContent);
            dataValidation.setShowPromptBox(true);
        }
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation)
        {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        else
        {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
        // 设置hiddenSheet隐藏
        wb.setSheetHidden(wb.getSheetIndex(hideSheet), true);
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     * 
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @param separator 分隔符
     * @return 解析后值
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(SEPARATOR);
        for (String item : convertSource)
        {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator))
            {
                for (String value : propertyValue.split(separator))
                {
                    if (itemArray[0].equals(value))
                    {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            }
            else
            {
                if (itemArray[0].equals(propertyValue))
                {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     * 
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @param separator 分隔符
     * @return 解析后值
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(SEPARATOR);
        for (String item : convertSource)
        {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator))
            {
                for (String value : propertyValue.split(separator))
                {
                    if (itemArray[1].equals(value))
                    {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            }
            else
            {
                if (itemArray[1].equals(propertyValue))
                {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 解析字典值
     * 
     * @param dictValue 字典值
     * @param dictType 字典类型
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String convertDictByExp(String dictValue, String dictType, String separator)
    {
        return DictUtils.getDictLabel(dictType, dictValue, separator);
    }

    /**
     * 反向解析值字典值
     * 
     * @param dictLabel 字典标签
     * @param dictType 字典类型
     * @param separator 分隔符
     * @return 字典值
     */
    public static String reverseDictByExp(String dictLabel, String dictType, String separator)
    {
        return DictUtils.getDictValue(dictType, dictLabel, separator);
    }

    /**
     * 数据处理器
     * 
     * @param value 数据值
     * @param excel 数据注解
     * @return
     */
    public String dataFormatHandlerAdapter(Object value, Excel excel, Cell cell)
    {
        try
        {
            Object instance = excel.handler().newInstance();
            Method formatMethod = excel.handler().getMethod("format", new Class[] { Object.class, String[].class, Cell.class, Workbook.class });
            value = formatMethod.invoke(instance, value, excel.args(), cell, this.wb);
        }
        catch (Exception e)
        {
            log.error("不能格式化数据 " + excel.handler(), e.getMessage());
        }
        return Convert.toStr(value);
    }

    /**
     * 合计统计信息
     */
    private void addStatisticsData(Integer index, String text, Excel entity)
    {
        if (entity != null && entity.isStatistics())
        {
            Double temp = 0D;
            if (!statistics.containsKey(index))
            {
                statistics.put(index, temp);
            }
            try
            {
                temp = Double.valueOf(text);
            }
            catch (NumberFormatException e)
            {
            }
            statistics.put(index, statistics.get(index) + temp);
        }
    }

    /**
     * 创建统计行
     */
    public void addStatisticsRow()
    {
        if (statistics.size() > 0)
        {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = statistics.keySet();
            Cell cell = row.createCell(0);
            cell.setCellStyle(styles.get("total"));
            cell.setCellValue("合计");

            for (Integer key : keys)
            {
                cell = row.createCell(key);
                cell.setCellStyle(styles.get("total"));
                cell.setCellValue(DOUBLE_FORMAT.format(statistics.get(key)));
            }
            statistics.clear();
        }
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename)
    {
        return UUID.randomUUID() + "_" + filename + ".xlsx";
    }

    /**
     * 获取下载路径
     * 
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = com.omniperform.common.config.OmniperformConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 获取bean中的属性值
     * 
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception
    {
        field.setAccessible(true);
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            if (target.contains("."))
            {
                String[] targets = target.split("[.]");
                for (String name : targets)
                {
                    o = getValue(o, name);
                }
            }
            else
            {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     * 
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (StringUtils.isNotNull(o) && StringUtils.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return o;
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField()
    {
        this.fields = getFields();
        this.fields = this.fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).collect(Collectors.toList());
        this.maxHeight = getRowHeight();
    }

    /**
     * 获取字段注解信息
     */
    public List<Object[]> getFields()
    {
        List<Object[]> fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (StringUtils.isNotEmpty(includeFields))
        {
            for (Field field : tempFields)
            {
                if (ArrayUtils.contains(this.includeFields, field.getName()) || field.isAnnotationPresent(Excels.class))
                {
                    addField(fields, field);
                }
            }
        }
        else if (StringUtils.isNotEmpty(excludeFields))
        {
            for (Field field : tempFields)
            {
                if (!ArrayUtils.contains(this.excludeFields, field.getName()))
                {
                    addField(fields, field);
                }
            }
        }
        else
        {
            for (Field field : tempFields)
            {
                addField(fields, field);
            }
        }
        return fields;
    }

    /**
     * 添加字段信息
     */
    public void addField(List<Object[]> fields, Field field)
    {
        // 单注解
        if (field.isAnnotationPresent(Excel.class))
        {
            Excel attr = field.getAnnotation(Excel.class);
            if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
            {
                fields.add(new Object[] { field, attr });
            }
            if (Collection.class.isAssignableFrom(field.getType()))
            {
                subMethod = getSubMethod(field.getName(), clazz);
                ParameterizedType pt = (ParameterizedType) field.getGenericType();
                Class<?> subClass = (Class<?>) pt.getActualTypeArguments()[0];
                this.subFields = FieldUtils.getFieldsListWithAnnotation(subClass, Excel.class);
            }
        }

        // 多注解
        if (field.isAnnotationPresent(Excels.class))
        {
            Excels attrs = field.getAnnotation(Excels.class);
            Excel[] excels = attrs.value();
            for (Excel attr : excels)
            {
                if (StringUtils.isNotEmpty(includeFields))
                {
                    if (ArrayUtils.contains(this.includeFields, field.getName() + "." + attr.targetAttr())
                            && (attr != null && (attr.type() == Type.ALL || attr.type() == type)))
                    {
                        fields.add(new Object[] { field, attr });
                    }
                }
                else
                {
                    if (!ArrayUtils.contains(this.excludeFields, field.getName() + "." + attr.targetAttr())
                            && (attr != null && (attr.type() == Type.ALL || attr.type() == type)))
                    {
                        fields.add(new Object[] { field, attr });
                    }
                }
            }
        }
    }

    /**
     * 根据注解获取最大行高
     */
    public short getRowHeight()
    {
        double maxHeight = 0;
        for (Object[] os : this.fields)
        {
            Excel excel = (Excel) os[1];
            maxHeight = Math.max(maxHeight, excel.height());
        }
        return (short) (maxHeight * 20);
    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
        this.sheet = wb.createSheet();
        wb.setSheetName(0, sheetName);
        this.styles = createStyles(wb);
    }

    /**
     * 创建工作表
     * 
     * @param sheetNo sheet数量
     * @param index 序号
     */
    public void createSheet(int sheetNo, int index)
    {
        // 设置工作表的名称.
        if (sheetNo > 1 && index > 0)
        {
            this.sheet = wb.createSheet();
            this.createTitle();
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 获取单元格值
     * 
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            log.warn("Excel解析 - 获取单元格值失败: 行为null, 列={}", column);
            return "";
        }
        
        Object val = "";
        Cell cell = null;
        
        try
        {
            cell = row.getCell(column);
            if (cell == null)
            {
                log.debug("Excel解析 - 单元格为空: 行={}, 列={}", row.getRowNum(), column);
                return "";
            }
            
            CellType cellType = cell.getCellType();
            log.debug("Excel解析 - 读取单元格: 行={}, 列={}, 类型={}", row.getRowNum(), column, cellType);
            
            switch (cellType)
            {
                case NUMERIC:
                case FORMULA:
                    try 
                    {
                        val = cell.getNumericCellValue();
                        if (DateUtil.isCellDateFormatted(cell))
                        {
                            val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                            log.debug("Excel解析 - 日期值转换: 行={}, 列={}, 原值={}, 转换后={}", 
                                    row.getRowNum(), column, cell.getNumericCellValue(), val);
                        }
                        else
                        {
                            Double numVal = (Double) val;
                            if (numVal % 1 != 0)
                            {
                                val = new BigDecimal(val.toString());
                            }
                            else
                            {
                                val = new DecimalFormat("0").format(val);
                            }
                            log.debug("Excel解析 - 数值转换: 行={}, 列={}, 原值={}, 转换后={}", 
                                    row.getRowNum(), column, cell.getNumericCellValue(), val);
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        log.warn("Excel解析 - 数值格式错误: 行={}, 列={}, 错误={}", 
                                row.getRowNum(), column, e.getMessage());
                        val = cell.toString(); // 回退到字符串值
                    }
                    break;
                    
                case STRING:
                    val = cell.getStringCellValue();
                    if (val != null && ((String) val).trim().isEmpty())
                    {
                        log.debug("Excel解析 - 字符串为空: 行={}, 列={}", row.getRowNum(), column);
                        val = "";
                    }
                    break;
                    
                case BOOLEAN:
                    val = cell.getBooleanCellValue();
                    log.debug("Excel解析 - 布尔值: 行={}, 列={}, 值={}", row.getRowNum(), column, val);
                    break;
                    
                case ERROR:
                    val = cell.getErrorCellValue();
                    log.warn("Excel解析 - 单元格错误: 行={}, 列={}, 错误码={}", 
                            row.getRowNum(), column, val);
                    val = ""; // 错误单元格返回空字符串
                    break;
                    
                case BLANK:
                    log.debug("Excel解析 - 空白单元格: 行={}, 列={}", row.getRowNum(), column);
                    val = "";
                    break;
                    
                default:
                    log.warn("Excel解析 - 未知单元格类型: 行={}, 列={}, 类型={}", 
                            row.getRowNum(), column, cellType);
                    val = cell.toString();
                    break;
            }
        }
        catch (Exception e)
        {
            log.error("Excel解析 - 获取单元格值异常: 行={}, 列={}, 异常={}", 
                    row != null ? row.getRowNum() : -1, column, e.getMessage(), e);
            // 尝试获取单元格的字符串表示作为回退
            try 
            {
                if (cell != null)
                {
                    val = cell.toString();
                    log.info("Excel解析 - 异常回退成功: 行={}, 列={}, 回退值={}", 
                            row.getRowNum(), column, val);
                }
            }
            catch (Exception fallbackException)
            {
                log.error("Excel解析 - 回退也失败: 行={}, 列={}, 回退异常={}", 
                        row.getRowNum(), column, fallbackException.getMessage());
                val = "";
            }
        }
        
        return val;
    }

    /**
     * 转换字段值为目标类型
     * 
     * @param val 原始值
     * @param fieldType 目标字段类型
     * @param field 字段对象
     * @param attr Excel注解
     * @param rowNum 行号
     * @param columnNum 列号
     * @return 转换后的值
     */
    private Object convertFieldValue(Object val, Class<?> fieldType, Field field, Excel attr, int rowNum, int columnNum)
    {
        try 
        {
            if (val == null || "".equals(val))
            {
                log.debug("Excel解析 - 空值转换: 行={}, 列={}, 字段类型={}", rowNum, columnNum, fieldType.getSimpleName());
                return getDefaultValue(fieldType);
            }
            
            if (String.class == fieldType)
            {
                String s = Convert.toStr(val);
                if (s.matches("^\\d+\\.0$"))
                {
                    val = StringUtils.substringBefore(s, ".0");
                    log.debug("Excel解析 - 字符串转换(去除.0): 行={}, 列={}, 原值={}, 转换后={}", 
                            rowNum, columnNum, s, val);
                }
                else
                {
                    String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                    if (StringUtils.isNotEmpty(dateFormat))
                    {
                        val = parseDateToStr(dateFormat, val);
                        log.debug("Excel解析 - 日期字符串转换: 行={}, 列={}, 格式={}, 转换后={}", 
                                rowNum, columnNum, dateFormat, val);
                    }
                    else
                    {
                        val = Convert.toStr(val);
                    }
                }
            }
            else if (Integer.TYPE == fieldType || Integer.class == fieldType)
            {
                String strVal = Convert.toStr(val);
                if (StringUtils.isNumeric(strVal))
                {
                    val = Convert.toInt(val);
                    log.debug("Excel解析 - 整数转换: 行={}, 列={}, 原值={}, 转换后={}", 
                            rowNum, columnNum, strVal, val);
                }
                else if (StringUtils.isEmpty(strVal))
                {
                    val = fieldType.isPrimitive() ? 0 : null;
                    log.debug("Excel解析 - 整数空值处理: 行={}, 列={}, 设置为={}", rowNum, columnNum, val);
                }
                else
                {
                    throw new NumberFormatException("无法将 '" + strVal + "' 转换为整数");
                }
            }
            else if (Long.TYPE == fieldType || Long.class == fieldType)
            {
                String strVal = Convert.toStr(val);
                if (StringUtils.isNumeric(strVal))
                {
                    val = Convert.toLong(val);
                    log.debug("Excel解析 - 长整数转换: 行={}, 列={}, 原值={}, 转换后={}", 
                            rowNum, columnNum, strVal, val);
                }
                else if (StringUtils.isEmpty(strVal))
                {
                    val = fieldType.isPrimitive() ? 0L : null;
                    log.debug("Excel解析 - 长整数空值处理: 行={}, 列={}, 设置为={}", rowNum, columnNum, val);
                }
                else
                {
                    throw new NumberFormatException("无法将 '" + strVal + "' 转换为长整数");
                }
            }
            else if (Double.TYPE == fieldType || Double.class == fieldType)
            {
                try 
                {
                    val = Convert.toDouble(val);
                    log.debug("Excel解析 - 双精度转换: 行={}, 列={}, 转换后={}", rowNum, columnNum, val);
                }
                catch (Exception e)
                {
                    val = fieldType.isPrimitive() ? 0.0 : null;
                    log.warn("Excel解析 - 双精度转换失败，使用默认值: 行={}, 列={}, 默认值={}", rowNum, columnNum, val);
                }
            }
            else if (Float.TYPE == fieldType || Float.class == fieldType)
            {
                try 
                {
                    val = Convert.toFloat(val);
                    log.debug("Excel解析 - 浮点数转换: 行={}, 列={}, 转换后={}", rowNum, columnNum, val);
                }
                catch (Exception e)
                {
                    val = fieldType.isPrimitive() ? 0.0f : null;
                    log.warn("Excel解析 - 浮点数转换失败，使用默认值: 行={}, 列={}, 默认值={}", rowNum, columnNum, val);
                }
            }
            else if (BigDecimal.class == fieldType)
            {
                try 
                {
                    val = Convert.toBigDecimal(val);
                    log.debug("Excel解析 - BigDecimal转换: 行={}, 列={}, 转换后={}", rowNum, columnNum, val);
                }
                catch (Exception e)
                {
                    val = null;
                    log.warn("Excel解析 - BigDecimal转换失败，设置为null: 行={}, 列={}", rowNum, columnNum);
                }
            }
            else if (Date.class == fieldType)
            {
                if (val instanceof String)
                {
                    val = DateUtils.parseDate(val);
                    log.debug("Excel解析 - 日期转换(字符串): 行={}, 列={}, 转换后={}", rowNum, columnNum, val);
                }
                else if (val instanceof Double)
                {
                    val = DateUtil.getJavaDate((Double) val);
                    log.debug("Excel解析 - 日期转换(数值): 行={}, 列={}, 转换后={}", rowNum, columnNum, val);
                }
                else
                {
                    log.warn("Excel解析 - 不支持的日期类型: 行={}, 列={}, 值类型={}", 
                            rowNum, columnNum, val.getClass().getSimpleName());
                    val = null;
                }
            }
            else if (Boolean.TYPE == fieldType || Boolean.class == fieldType)
            {
                val = Convert.toBool(val, fieldType.isPrimitive() ? false : null);
                log.debug("Excel解析 - 布尔值转换: 行={}, 列={}, 转换后={}", rowNum, columnNum, val);
            }
            else
            {
                log.debug("Excel解析 - 未处理的字段类型: 行={}, 列={}, 类型={}, 保持原值", 
                        rowNum, columnNum, fieldType.getSimpleName());
            }
        }
        catch (Exception e)
        {
            log.error("Excel解析 - 字段值转换异常: 行={}, 列={}, 字段类型={}, 异常={}", 
                    rowNum, columnNum, fieldType.getSimpleName(), e.getMessage());
            throw e;
        }
        
        return val;
    }

    /**
     * 应用转换器和字典映射
     * 
     * @param val 值
     * @param attr Excel注解
     * @param row 行对象
     * @param columnNum 列号
     * @return 处理后的值
     */
    private Object applyConvertersAndDictionary(Object val, Excel attr, Row row, int columnNum)
    {
        try 
        {
            if (StringUtils.isNotEmpty(attr.readConverterExp()))
            {
                val = reverseByExp(Convert.toStr(val), attr.readConverterExp(), attr.separator());
                log.debug("Excel解析 - 表达式转换: 行={}, 列={}, 表达式={}, 转换后={}", 
                        row.getRowNum(), columnNum, attr.readConverterExp(), val);
            }
            else if (StringUtils.isNotEmpty(attr.dictType()))
            {
                String dictKey = attr.dictType() + val;
                if (!sysDictMap.containsKey(dictKey))
                {
                    String dictValue = reverseDictByExp(Convert.toStr(val), attr.dictType(), attr.separator());
                    sysDictMap.put(dictKey, dictValue);
                    log.debug("Excel解析 - 字典映射缓存: 行={}, 列={}, 字典类型={}, 键={}, 值={}", 
                            row.getRowNum(), columnNum, attr.dictType(), val, dictValue);
                }
                val = sysDictMap.get(dictKey);
                log.debug("Excel解析 - 字典映射: 行={}, 列={}, 字典类型={}, 转换后={}", 
                        row.getRowNum(), columnNum, attr.dictType(), val);
            }
            else if (!attr.handler().equals(ExcelHandlerAdapter.class))
            {
                val = dataFormatHandlerAdapter(val, attr, null);
                log.debug("Excel解析 - 自定义处理器: 行={}, 列={}, 处理器={}, 转换后={}", 
                        row.getRowNum(), columnNum, attr.handler().getSimpleName(), val);
            }
            else if (ColumnType.IMAGE == attr.cellType())
            {
                // 图片处理功能暂时禁用，因为pictures变量需要从外部传入
                val = "";
                log.debug("Excel解析 - 图片处理: 行={}, 列={}, 图片处理功能暂时禁用", 
                        row.getRowNum(), columnNum);
            }
        }
        catch (Exception e)
        {
            log.error("Excel解析 - 转换器/字典处理异常: 行={}, 列={}, 异常={}", 
                    row.getRowNum(), columnNum, e.getMessage());
            // 发生异常时返回原值
        }
        
        return val;
    }

    /**
     * 获取字段类型的默认值
     * 
     * @param fieldType 字段类型
     * @return 默认值
     */
    private Object getDefaultValue(Class<?> fieldType)
    {
        if (fieldType.isPrimitive())
        {
            if (fieldType == int.class) return 0;
            if (fieldType == long.class) return 0L;
            if (fieldType == double.class) return 0.0;
            if (fieldType == float.class) return 0.0f;
            if (fieldType == boolean.class) return false;
            if (fieldType == byte.class) return (byte) 0;
            if (fieldType == short.class) return (short) 0;
            if (fieldType == char.class) return '\0';
        }
        return null;
    }

    /**
     * 判断是否是空行
     * 
     * @param row 判断的行
     * @return
     */
    private boolean isRowEmpty(Row row)
    {
        if (row == null)
        {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
        {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取Excel2003图片
     *
     * @param sheet 当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（1_1）String，value:图片流PictureData
     */
    public static Map<String, List<PictureData>> getSheetPictures03(HSSFSheet sheet, HSSFWorkbook workbook)
    {
        Map<String, List<PictureData>> sheetIndexPicMap = new HashMap<>();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (!pictures.isEmpty() && sheet.getDrawingPatriarch() != null)
        {
            for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren())
            {
                if (shape instanceof HSSFPicture)
                {
                    HSSFPicture pic = (HSSFPicture) shape;
                    HSSFClientAnchor anchor = (HSSFClientAnchor) pic.getAnchor();
                    String picIndex = anchor.getRow1() + "_" + anchor.getCol1();
                    sheetIndexPicMap.computeIfAbsent(picIndex, k -> new ArrayList<>()).add(pic.getPictureData());
                }
            }
        }
        return sheetIndexPicMap;
    }

    /**
     * 获取Excel2007图片
     *
     * @param sheet 当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（1_1）String，value:图片流PictureData
     */
    public static Map<String, List<PictureData>> getSheetPictures07(XSSFSheet sheet, XSSFWorkbook workbook)
    {
        Map<String, List<PictureData>> sheetIndexPicMap = new HashMap<>();
        for (POIXMLDocumentPart dr : sheet.getRelations())
        {
            if (dr instanceof XSSFDrawing)
            {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                for (XSSFShape shape : drawing.getShapes())
                {
                    if (shape instanceof XSSFPicture)
                    {
                        XSSFPicture pic = (XSSFPicture) shape;
                        XSSFClientAnchor anchor = pic.getPreferredSize();
                        CTMarker ctMarker = anchor.getFrom();
                        String picIndex = ctMarker.getRow() + "_" + ctMarker.getCol();
                        sheetIndexPicMap.computeIfAbsent(picIndex, k -> new ArrayList<>()).add(pic.getPictureData());
                    }
                }
            }
        }
        return sheetIndexPicMap;
    }

    /**
     * 格式化不同类型的日期对象
     * 
     * @param dateFormat 日期格式
     * @param val 被格式化的日期对象
     * @return 格式化后的日期字符
     */
    public String parseDateToStr(String dateFormat, Object val)
    {
        if (val == null)
        {
            return "";
        }
        String str;
        if (val instanceof Date)
        {
            str = DateUtils.parseDateToStr(dateFormat, (Date) val);
        }
        else if (val instanceof LocalDateTime)
        {
            str = DateUtils.parseDateToStr(dateFormat, DateUtils.toDate((LocalDateTime) val));
        }
        else if (val instanceof LocalDate)
        {
            str = DateUtils.parseDateToStr(dateFormat, DateUtils.toDate((LocalDate) val));
        }
        else
        {
            str = val.toString();
        }
        return str;
    }

    /**
     * 是否有对象的子列表
     */
    public boolean isSubList()
    {
        return StringUtils.isNotNull(subFields) && subFields.size() > 0;
    }

    /**
     * 是否有对象的子列表，集合不为空
     */
    public boolean isSubListValue(T vo)
    {
        return StringUtils.isNotNull(subFields) && subFields.size() > 0 && StringUtils.isNotNull(getListCellValue(vo)) && getListCellValue(vo).size() > 0;
    }

    /**
     * 获取集合的值
     */
    public Collection<?> getListCellValue(Object obj)
    {
        Object value;
        try
        {
            value = subMethod.invoke(obj, new Object[] {});
        }
        catch (Exception e)
        {
            return new ArrayList<Object>();
        }
        return (Collection<?>) value;
    }

    /**
     * 获取对象的子列表方法
     * 
     * @param name 名称
     * @param pojoClass 类对象
     * @return 子列表方法
     */
    public Method getSubMethod(String name, Class<?> pojoClass)
    {
        StringBuffer getMethodName = new StringBuffer("get");
        getMethodName.append(name.substring(0, 1).toUpperCase());
        getMethodName.append(name.substring(1));
        Method method = null;
        try
        {
            method = pojoClass.getMethod(getMethodName.toString(), new Class[] {});
        }
        catch (Exception e)
        {
            log.error("获取对象异常{}", e.getMessage());
        }
        return method;
    }
}
