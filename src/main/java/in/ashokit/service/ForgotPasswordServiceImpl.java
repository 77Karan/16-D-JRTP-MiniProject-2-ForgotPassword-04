 package in.ashokit.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.constants.AppConstants;
import in.ashokit.entities.UserEntity;
import in.ashokit.props.AppProperties;
import in.ashokit.repositories.UserRepository;
import in.ashokit.utils.EmailUtils;
@Service
public class ForgotPasswordServiceImpl implements ForgotPaawordService
{
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AppProperties appProps;
	
	@Autowired
	private EmailUtils emailUtils;

	@Override
	public String forgotPassword(String email)
	{
		
		UserEntity userEntity =userRepo.findByUserEmail(email);
		if(null == userEntity)
		{
			return appProps.getMessages().get(AppConstants.INVALID_EMAIL_ID);
			
		}
			sendForgotEmail(userEntity);
			
		return appProps.getMessages().get(AppConstants.SUCESS);
	}
	
	
	
	private boolean sendForgotEmail(UserEntity userEntity)
	{
		boolean emailSent = false;
		String subject = appProps.getMessages().get("emailSubject");
		String bodyFileName = appProps.getMessages().get("emailBodyFile");
		String body=readMailBody(bodyFileName, userEntity);
		if(emailUtils.sendEmail(subject, body, userEntity.getUserEmail()))
		{
			emailSent=true;
			return emailSent;
		}
		return emailSent;
	}
	
	private String readMailBody(String fileName,UserEntity userEntity)
	{
		String mailBody = null;
		StringBuffer buffer = new StringBuffer();
		Path path = Paths.get(fileName);	
		String userName = userEntity.getUserFirstName();
		String userPwd = userEntity.getUserPwd();
		try (Stream<String> stream = Files.lines(path))
		{
			stream.forEach(line -> { buffer.append(line); });
			mailBody = buffer.toString();
			mailBody = mailBody.replace(AppConstants.FNAME,userName);
			mailBody = mailBody.replace(AppConstants.PWD,userPwd);

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return mailBody;
	}

}
