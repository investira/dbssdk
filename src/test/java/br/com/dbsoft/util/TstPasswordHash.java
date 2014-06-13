package br.com.dbsoft.util;

import org.junit.Assert;
import org.junit.Test;

import br.com.dbsoft.util.DBSPassword;


public class TstPasswordHash {


	@Test
	public void testeSaltedPassword(){
		String xPassword = DBSPassword.createSaltedPassword("dbsoft");
		Assert.assertEquals(true, DBSPassword.validateSaltedPassword("dbsoft", xPassword));
	}
	
	@Test
	public void testePassword(){
		String xPassword = DBSPassword.createPassword("dbsoft");
		Assert.assertEquals(true, DBSPassword.validatePassword("dbsoft", xPassword));
	}
	
	
}
