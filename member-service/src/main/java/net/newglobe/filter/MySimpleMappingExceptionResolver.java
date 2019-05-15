package net.newglobe.filter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.alibaba.fastjson.JSON;

import net.newglobe.app.model.vo.DataResult;

public class MySimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String viewName = determineViewName(ex, request);
		Integer statusCode = determineStatusCode(request, viewName);
		try {
			ModelAndView mv = new ModelAndView();
			/* 使用response返回 */
			response.setStatus(HttpStatus.OK.value()); // 设置状态码,这里是为了解决unauthorizedexception一直在控制台显示的问题
			response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
			response.setCharacterEncoding("UTF-8"); // 避免乱码

			DataResult<String> result = new DataResult<String>();
			result.setSuccess(false);
			result.setData(ex.getMessage());
			result.setMessage("遇到异常！错误代码:" + statusCode);
			PrintWriter writer = response.getWriter();
			writer.write(JSON.toJSONString(result));
			writer.flush();
			writer.close();
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}