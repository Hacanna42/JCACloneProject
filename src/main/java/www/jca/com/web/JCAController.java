package www.jca.com.web;

import org.springframework.beans.factory.annotation.Autowired;

import www.jca.com.security.AuthenticationFacade;
import www.jca.com.service.UserService;
import www.jca.com.vo.UserVO;

public class JCAController {
	@Autowired
	protected AuthenticationFacade authenticationFacade;
	@Autowired
	protected UserService userService;	
	protected UserVO getUser() {
		String authUser = authenticationFacade.getAuthentication().getName();
		
		UserVO searchUser = new UserVO();
		searchUser.setLogin(authUser);
		UserVO user = userService.selectOne(searchUser);
		
		return user;
	}
}
