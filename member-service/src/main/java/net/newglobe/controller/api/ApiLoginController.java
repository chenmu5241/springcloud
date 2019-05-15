package net.newglobe.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.newglobe.app.model.SysAccount;
import net.newglobe.app.model.SysPower;
import net.newglobe.app.model.SysRole;
import net.newglobe.app.model.vo.DataResult;
import net.newglobe.app.model.vo.Result;
import net.newglobe.app.service.SysAccountService;
import net.newglobe.app.service.SysPowerService;
import net.newglobe.app.service.SysRoleService;
import net.newglobe.util.Md5;
import net.newglobe.util.TokenUtil;

@Api(tags = "1、登录接口",position=2)
@RequestMapping("api/login")
@RestController
public class ApiLoginController {

	public Logger logger = LoggerFactory.getLogger("ApiLoginController");
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private DefaultWebSessionManager shiroSessionManager;
	@Autowired
	private SysAccountService sysAccountService;
	@Autowired
	private SysRoleService roleService;
	@Autowired
	private SysPowerService powerService;

	@ApiOperation(value = "登录获取token")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "String", name = "token", value = "token不需要", required = false, defaultValue = ""),
			@ApiImplicitParam(paramType = "query", dataType = "String", name = "username", value = "用户名", required = true, defaultValue = "root"),
			@ApiImplicitParam(paramType = "query", dataType = "String", name = "password", value = "密码", required = true, defaultValue = "123456") })
	@RequestMapping(value = "log", method = RequestMethod.POST)
	public DataResult<String> apiLogin(HttpServletRequest request, HttpServletResponse response, String username,
			String password) {
		DataResult<String> result = new DataResult<String>();
		try {
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			token.setRememberMe(true);
			subject.login(token);// 如果登录成功才会继续执行下面的操作
			// 设置token
			String tokenStr = tokenUtil.createAndSaveToken(subject);// 设置token

			// 获取用户权限
			SysAccount sysAccount = (SysAccount) SecurityUtils.getSubject().getPrincipal();
			if (sysAccount != null) {
				// 设置用户的角色和权限
				boolean flag = false;
				// 查询用户拥有的角色的权限
				List<SysRole> roles = roleService.getAccountRoles(sysAccount.getId());
				for (SysRole role : roles) {
					if (role.getId() == 1) {
						flag = true;
						break;
					}
				}

				List<SysPower> powers = null;
				SysPower power = new SysPower();
				power.setStatus(1);
				power.setIsShow(0);
				if (flag) {// 超级管理员拥有所有权限
					powers = powerService.selectList(power);
				} else {
					powers = powerService.getPowersByAccountId(sysAccount.getId());
				}
				sysAccount.setRoot(flag);
				sysAccount.setPowers(powers);

				String projectId = request.getParameter("projectId");
			}
			result.setData(tokenStr);
			result.setSuccess(true);
			result.setMessage("登录成功!");
		} catch (Exception e) {
			e.printStackTrace();
			if (UnknownAccountException.class.getName().equals(e.getClass().getName())) {
				// 最终会抛给异常处理器
				result.setMessage("账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(e.getClass().getName())) {
				result.setMessage("用户名/密码错误");
			} else if ("randomCodeError".equals(e.getClass().getName())) {
				result.setMessage("验证码错误");
			} else if ("longTimeSession".equals(e.getClass().getName())) {
				result.setMessage("session无效刷新重试");
			} else if (NullPointerException.class.getName().equals(e.getClass().getName())) {
				result.setMessage("用户名密码不能为空");
			} else {
				result.setMessage("其他错误");
			}
			result.setSuccess(false);
			// logger.error("登录异常:", e);
		}
		return result;
	}

	@ApiOperation(value = "判断token是否有效",position=1)
	@RequestMapping(value = "api/isLogin", method = RequestMethod.POST)
	public Result isLogin(String token) {
		Result result = new Result();
		try {
			Subject subject = tokenUtil.getSubject(token);
			if (subject == null) {
				result.setSuccess(false);
				result.setMessage("token无效！");
			} else {
				if ((System.currentTimeMillis()
						- subject.getSession().getStartTimestamp().getTime()) >= shiroSessionManager
								.getGlobalSessionTimeout() - 1000) {
					tokenUtil.removeToken(token);
					result.setSuccess(false);
					result.setMessage("token无效！");
					return result;
				}

				if (subject.isAuthenticated()) {
					result.setSuccess(true);
					result.setMessage("有效token！");
				} else {
					result.setSuccess(false);
					result.setMessage("token无效！");
				}
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("验证token失败!");
			logger.error("ApiLoginController.isLogin方法执行遇到异常", e);
		}
		return result;
	}

	@ApiOperation(value = "校验手机号是否已存在",position=2)
	@RequestMapping(value = "api/checkPhone", method = RequestMethod.POST)
	public Result checkPhone(String phone) {
		Result result = new Result();
		try {
			SysAccount account = new SysAccount();
			account.setPhone(phone);
			account.setStatus(1);
			Long count = sysAccountService.selectCount(account);
			if (count == 0) {
				result.setMessage("手机号未注册！");
				result.setSuccess(false);
			} else {
				result.setSuccess(true);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("验证手机号异常!");
			logger.error("ApiLoginController.checkPhone方法执行遇到异常", e);
		}
		return result;
	}

	@ApiOperation(value = "重置密码",position=3)
	@RequestMapping(value = "api/resetNewPwd", method = RequestMethod.POST)
	public Result resetNewPwd(SysAccount account) {
		Result result = new Result();
		try {
			SysAccount t = new SysAccount();
			t.setPhone(account.getPhone());
			t.setStatus(1);
			SysAccount existCount = sysAccountService.selectOne(t);
			if (existCount == null) {
				result.setMessage("手机号未注册！");
				result.setSuccess(false);
			} else {// 修改新的密码
				existCount.setSalt(Md5.getSalt());
				existCount.setPassword(Md5.getMD5(account.getPassword(), existCount.getSalt()));
				sysAccountService.updateByIdSelective(existCount);
				result.setSuccess(true);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage("重置密码异常!");
			logger.error("ApiLoginController.checkPhone方法执行遇到异常", e);
		}
		return result;
	}

}