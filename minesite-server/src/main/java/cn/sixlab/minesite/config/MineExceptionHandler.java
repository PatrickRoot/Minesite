package cn.sixlab.minesite.config;

import cn.sixlab.minesite.utils.Err;
import cn.sixlab.minesite.utils.ResultUtils;
import cn.sixlab.minesite.utils.WebUtils;
import cn.sixlab.minesite.vo.MineException;
import cn.sixlab.minesite.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

@Slf4j
@ControllerAdvice
public class MineExceptionHandler {

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MissingServletRequestPartException.class, MissingServletRequestParameterException.class, MultipartException.class})
    public ModelAndView handleHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException e) {
        log.error("参数解析异常", e);

        return errorMsg(request, response, ResultUtils.error(Err.EXCEPTION, "exception.parse"));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ModelAndView handleIllegalArgumentException(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException e) {
        log.error("参数异常", e);

        return errorMsg(request, response, ResultUtils.error(Err.EXCEPTION, "exception.parse"));
    }

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    public ModelAndView handleHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("参数类型不支持异常", e);

        return errorMsg(request, response, ResultUtils.error(Err.EXCEPTION, "exception.type.not.support"));
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        MineException mineException = getException(e);

        Result result;
        if (null != mineException) {
            result = ResultUtils.error(mineException.getCode(), mineException.getMsg());
        } else {
            result = ResultUtils.error(Err.EXCEPTION, "exception.null");
        }

        return errorMsg(request, response, result);
    }

    private MineException getException(Exception e) {
        MineException mineException = null;

        if (e instanceof InvocationTargetException) {
            InvocationTargetException ee = ((InvocationTargetException) e);
            Throwable exception = ee.getTargetException();
            log.error("", exception);

            if (exception instanceof MineException) {
                mineException = ((MineException) exception);
            }
        } else if (e instanceof MineException) {
            mineException = ((MineException) e);
            log.info("MyException： | " + mineException.getCode() + " | " + mineException.getMsg());
        }

        return mineException;
    }

    private ModelAndView errorMsg(HttpServletRequest request, HttpServletResponse response, Result result) {
        String method = request.getMethod();

        if ("get".equalsIgnoreCase(method)) {
            ModelAndView modelAndView = new ModelAndView("error");

            modelAndView.addObject("code", result.getCode());
            modelAndView.addObject("message", result.getMsg());

            return modelAndView;
        } else {
            WebUtils.writeJson(response, result.toString());
            return null;
        }
    }
}
