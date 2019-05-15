package net.newglobe.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.newglobe.app.model.SysAccount;

@Api(tags = "2、用户接口")
@RequestMapping("api/user")
@RestController
public class AccountController {
	
	@ApiOperation(value = "登录获取token",position=0)
	@RequestMapping(value = "api/getUser", method = RequestMethod.POST)
	public SysAccount getUser() {
		return null;
	}

}
