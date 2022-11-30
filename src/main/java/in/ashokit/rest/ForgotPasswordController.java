package in.ashokit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.service.ForgotPaawordService;

@RestController
public class ForgotPasswordController
{
	@Autowired
	private ForgotPaawordService forgotPaawordService;
	
	@PostMapping("/forgotpwd/{email}")
	public String forgotPwd(@PathVariable String email)
	{
		return forgotPaawordService.forgotPassword(email);
		
	}

}
