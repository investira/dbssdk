package br.com.dbsoft.util;


import org.junit.Assert;
import org.junit.Test;


public class TstPasswordHash {


//	@Test
	public void testeSaltedPassword(){
		String xPassword;
//		xPassword = DBSPassword.createSaltedPassword("dbs0ft-local");
//		System.out.println(xPassword);
//		Assert.assertEquals(true, DBSPassword.validateSaltedPassword("dbsoft", xPassword));
//		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 10);
//		System.out.println(xPassword.length());
//		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 20);
//		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 31);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 40);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 50);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 60);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 70);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 80);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 90);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 100);
		System.out.println(xPassword.length());
		xPassword = DBSPassword.createSaltedPassword("dbsoft" , 110);
		System.out.println(xPassword.length());
//		Assert.assertEquals(true, DBSPassword.validateSaltedPassword("dbsoft", xPassword));
	}
	

//	@Test
	public void testeCheck(){
		String xPwd = "";
		xPwd = "DBSOFT";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, true));
		
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, true));
		
		xPwd = "DBsOFT";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, true));
		
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, true));

		xPwd = "DBs0FT";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, true));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, true));
		
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, true));

		xPwd = "dbsoft";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, true));
		
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, true));

		xPwd = "dbs0ft";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, true));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, true));
		
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, true));

		xPwd = "12345t";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, true, true));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, false, false, true));
		
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, false, true));

		xPwd = "123-+=45t";
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));

		xPwd = "123-+=45tA";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 4, 15, true, true, true));
		
		//tamanho
		xPwd = "DBSOFT";
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 0, 6, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 1, 6, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 0, 7, false, false, false));
		Assert.assertEquals(true,DBSPassword.checkPasswordContent(xPwd, 1, 7, false, false, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 1, 5, false, false, false));
		Assert.assertEquals(false,DBSPassword.checkPasswordContent(xPwd, 0, 5, false, false, false));

	}
	
//	@Test
	public void testeCreatePassword(){
		String xPassword;
		xPassword = DBSPassword.createPassword("dbsoft");
		System.out.println(xPassword);
		Assert.assertEquals(true, DBSPassword.validatePassword("dbsoft", xPassword));
	}
	
//	@Test
	public void testeCreatePassword256(){
		String xPassword;
		System.out.println(DBSPassword.createPassword256("Teste"));
		System.out.println(DBSPassword.createPassword256("Teste123"));
		xPassword = DBSPassword.createPassword256("dbsoft");
		System.out.println(xPassword);
		Assert.assertEquals(true, DBSPassword.validatePassword256("dbsoft", xPassword));
	}
	
	@Test
	public void testeCreatePassword512(){
		String xPassword;
		xPassword = DBSPassword.createPassword512("dbsoft");
		System.out.println(xPassword);
		Assert.assertEquals(true, DBSPassword.validatePassword512("dbsoft", xPassword));
	}
}
