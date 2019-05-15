package net.newglobe.util;

import org.apache.shiro.subject.Subject;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import net.newglobe.app.model.SysAccount;
import net.newglobe.config.ApplicationContextUtils;

@Component
public class TokenUtil {
	private Cache tokenCache;

	public TokenUtil() {
		EhCacheCacheManager ehCacheCacheManager = ApplicationContextUtils.applicationContext.getBean(EhCacheCacheManager.class);
		Cache cache = ehCacheCacheManager.getCache("tokenCache");
		this.tokenCache = cache;
	}

	public Cache getTokenCache() {
		return tokenCache;
	}

	// 根据token获取subject
	public Subject getSubject(String token) throws Exception {
		return tokenCache.get(token,Subject.class);
	}

	// 生成token与subject的对应，并且返回token
	public String createAndSaveToken(Subject subject) {
		SysAccount account = (SysAccount) subject.getPrincipal();
		String tokenStr = Md5.getMD5(account.getUsername(), account.getSalt());
		tokenCache.put(tokenStr, subject);
		return tokenStr;
	}

	public void clear() {
		tokenCache.clear();
	}

	public void removeToken(String token) {
		tokenCache.evict(token);
	}
}
