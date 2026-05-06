package com.smartdine.handler;

import com.smartdine.constant.MessageConstant;
import com.smartdine.exception.BaseException;
import com.smartdine.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理系统中各类异常，返回标准化的错误响应
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务基础异常
     *
     * @param ex 业务异常
     * @return 错误响应
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(BaseException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理数据库唯一约束冲突异常
     * 例如：重复的用户名、手机号等
     *
     * @param ex SQL完整性约束冲突异常
     * @return 错误响应
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        log.error("数据库约束冲突: {}", message);
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + " 已存在";
            return Result.error(msg);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 处理参数校验异常（@Valid 校验失败）
     *
     * @param ex 方法参数校验异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> exceptionHandler(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("参数校验失败: {}", message);
        return Result.error(message);
    }

    /**
     * 处理参数绑定异常
     *
     * @param ex 参数绑定异常
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public Result<String> exceptionHandler(BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("参数绑定失败: {}", message);
        return Result.error(message);
    }

    /**
     * 处理约束校验异常（@RequestParam 等参数校验失败）
     *
     * @param ex 约束校验异常
     * @return 错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> exceptionHandler(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.error("参数约束校验失败: {}", message);
        return Result.error(message);
    }

    /**
     * 处理请求体不可读异常（通常是JSON格式错误）
     *
     * @param ex 请求体不可读异常
     * @return 错误响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> exceptionHandler(HttpMessageNotReadableException ex) {
        log.error("请求体格式错误: {}", ex.getMessage());
        return Result.error("请求体格式错误，请检查JSON格式");
    }

    /**
     * 处理参数类型不匹配异常
     *
     * @param ex 参数类型不匹配异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<String> exceptionHandler(MethodArgumentTypeMismatchException ex) {
        String message = String.format("参数 '%s' 类型不匹配，期望类型: %s",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "未知");
        log.error("参数类型不匹配: {}", message);
        return Result.error(message);
    }

    /**
     * 处理文件上传大小超限异常
     *
     * @param ex 文件大小超限异常
     * @return 错误响应
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<String> exceptionHandler(MaxUploadSizeExceededException ex) {
        log.error("文件上传大小超限: {}", ex.getMessage());
        return Result.error("上传文件大小超过限制，请上传更小的文件");
    }

    /**
     * 处理空指针异常
     *
     * @param ex 空指针异常
     * @return 错误响应
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> exceptionHandler(NullPointerException ex) {
        log.error("空指针异常: ", ex);
        return Result.error("系统内部错误，请联系管理员");
    }

    /**
     * 处理非法参数异常
     *
     * @param ex 非法参数异常
     * @return 错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> exceptionHandler(IllegalArgumentException ex) {
        log.error("非法参数: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理非法状态异常
     *
     * @param ex 非法状态异常
     * @return 错误响应
     */
    @ExceptionHandler(IllegalStateException.class)
    public Result<String> exceptionHandler(IllegalStateException ex) {
        log.error("非法状态: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理所有其他未捕获的异常
     *
     * @param ex 异常
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> exceptionHandler(Exception ex) {
        log.error("系统异常: ", ex);
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
