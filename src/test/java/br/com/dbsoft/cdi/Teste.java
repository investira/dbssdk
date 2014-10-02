package br.com.dbsoft.cdi;

import java.sql.Connection;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;


//@RunWith(Arquillian.class)
public class Teste {

	
	//É necessário adicionar as classes que serão utilizadas com "addClass" conforme exemplo abaixo 
	//@Deployment
//    public static JavaArchive createDeployment() {
//		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
//            .addClass(DBObjeto.class)  
//            .addClass(DBObjeto2.class)  
//            .addClass(DBObjetoFactory.class)
//            .addClass(DBObjetoFactory2.class)
//            .addClass(TstConnectionDBSoftFactory.class)
//            .addClass(TstDataSourceFactory.class)
//            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//		return jar;
//    }

	@Inject @Named("A")  
	DBObjeto xA;
	@Inject @Named("B")  
	DBObjeto xB;

	@Inject  
	DBObjeto2 xA2;

	//@Resource(mappedName="dbsoft")
	//private DataSource wDS;

	@Inject 
	Connection wCn;

	//@Test
	public void teste1(){
		System.out.println("ENTROU NO TESTE1==================");
		if (wCn == null){
			System.out.println("Connexão = NULO");
		}else{
			System.out.println("Connexão = OK " + wCn.toString() );
		}
//		if (wDS == null){
//			System.out.println("DATASOURCE = NULO");
//		}else{
//			System.out.println("DATASOURCE = OK");
//		}
		Assert.assertEquals("ALO 2",  xA2.getTexto());
		Assert.assertEquals("ALO",  xA.getTexto());
		Assert.assertEquals("BYE",  xB.getTexto());
		System.out.println("SAIU DO ==========================");
	}
}
