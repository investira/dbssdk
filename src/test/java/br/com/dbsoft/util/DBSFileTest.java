package br.com.dbsoft.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class DBSFileTest {

	//@Test
	public void testaZiparArquivo() throws IOException {
		assertTrue(DBSFile.zipFile("/home/jose_addario/TesteZipFolder/teste.zip",
				"/home/jose_addario/TesteZipFolder/EMISSOR.TXT"));
	}

	@Test
	@Ignore
	public void testaDescompactarArquivo() throws IOException {
		System.out.println(DBSFile.unzip("/home/jose_addario/BDI/bdi05151744.zip",
				"/home/jose_addario/BDI/BDI05151733/"));
		
	}

	@Test
	@Ignore
	public void testaMoverArquivo() {
		assertTrue(DBSFile.move("/home/jose_addario/TesteMover/Teste3",
				"/home/jose_addario/DestinoTeste"));
	}

	@Test
	@Ignore
	public void testaDeletarArquivo() {
		assertTrue(DBSFile.delete("/home/jose_addario/TesteCopia/BDIN"));
	}

	@Test
	@Ignore
	public void testaCopiarArquivo() throws IOException {
		System.out.println(DBSFile.copy("/home/jose_addario/TesteCopia/Teste5",
				"/home/jose_addario/TesteCopia/Teste5(copia)"));
	}

	@Test
	@Ignore
	public void testaDownload() throws IOException {
		assertTrue(DBSFile
				.download(
						"http://www.bmfbovespa.com.br/fechamento-pregao/bdi/bdi0712.zip",
						"/home/jose_addario/TesteCopia/bdi"));
	}

	// ftp://user:password@host:port/filePathDestino;type=i.
	@Test
	@Ignore
	public void testaUpload() {
		assertTrue(DBSFile.upload(
				"ftp://dbsoft1:dbs0ft@www.dbsoft.com.br:21/tmp/BDIN;type=i",
				"/home/jose_addario/TesteCopia/BDIN"));
	}

	@Test
	@Ignore
	public void testaSeExiste() {
		assertTrue(DBSFile.exists("/home/jose_addario/teste"));
		System.out.println(DBSFile.exists("/home/jose_addario/teste"));
	}
	
	@Test
	public void testaPath(){
		assertEquals(DBSFile.getPath("http://www.abcd.com/cs"), "cs/");
		assertEquals(DBSFile.getPath("/abcd/cs"), "abcd/cs/");
		assertEquals(DBSFile.getPath("/abcd/cs/"), "abcd/cs/");
		assertEquals(DBSFile.getPath("abcd/cs/"), "abcd/cs/");
		assertEquals(DBSFile.getPath("http://www.abcd.com"), "/");
		assertEquals(DBSFile.getPath(""), "/");
		assertEquals(DBSFile.getPath("abcd//cs//"), "abcd/cs/");
		assertEquals(DBSFile.getPath("//e"), "/");
		assertEquals(DBSFile.getPath("e//"), "e/");
	}

}
