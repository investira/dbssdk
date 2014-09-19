package br.com.dbsoft.util;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;


import br.com.dbsoft.util.DBSFile.SORT_BY;
import br.com.dbsoft.util.DBSFile.SORT_ORDER;

public class TstFile {

	//@Test
	public void testaZiparArquivo() throws IOException {
		assertTrue(DBSFile.zipFile("/home/jose_addario/TesteZipFolder/teste.zip",
				"/home/jose_addario/TesteZipFolder/EMISSOR.TXT"));
	}

	@Test
	@Ignore
	public void testaDescompactarArquivo() throws IOException {
//		System.out.println(DBSFile.unzipFile("/home/jose_addario/BDI/bdi05151744.zip",
//				"/home/jose_addario/BDI/BDI05151733/"));
		
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
				"ftp://usuario:senha@www.dbsoft.com.br:21/tmp/BDIN;type=i",
				"/home/jose_addario/TesteCopia/BDIN"));
	}

	@Test
	@Ignore
	public void testaSeExiste() {
		assertTrue(DBSFile.exists("/home/jose_addario/teste"));
		System.out.println(DBSFile.exists("/home/jose_addario/teste"));
	}

	@Test
	public void testaPathFromFileName(){
		assertEquals(DBSFile.getPathFromFileName("http://www.abcd.com/cs"), "http://www.abcd.com/");
		assertEquals(DBSFile.getPathFromFileName("http://www.abcd.com/cs/abcd"), "http://www.abcd.com/cs/");
		assertEquals(DBSFile.getPathFromFileName("/abcd/cs"), "/abcd/");
		assertEquals(DBSFile.getPathFromFileName("abcd"), "/");
		assertEquals(DBSFile.getPathFromFileName("abcd/"), "/abcd/");
		assertEquals(DBSFile.getPathFromFileName("/abcd/cs/"), "/abcd/cs/");
		assertEquals(DBSFile.getPathFromFileName("abcd/cs/"), "/abcd/cs/");
		assertEquals(DBSFile.getPathFromFileName("http://www.abcd.com"), "http://www.abcd.com/");
		assertEquals(DBSFile.getPathFromFileName(""), "/");
		assertEquals(DBSFile.getPathFromFileName("abcd//cs//"), "/abcd/cs/");
		assertEquals(DBSFile.getPathFromFileName("//e"), "//e/");
		assertEquals(DBSFile.getPathFromFileName("e//"), "/e/");
	}

	@Test
	public void testaPathFromFolderName(){
		assertEquals(DBSFile.getPathFromFolderName("http://www.abcd.com/cs"), "http://www.abcd.com/cs/");
		assertEquals(DBSFile.getPathFromFolderName("http://www.abcd.com/cs/abcd"), "http://www.abcd.com/cs/abcd/");
		assertEquals(DBSFile.getPathFromFolderName("/abcd/cs"), "/abcd/cs/");
		assertEquals(DBSFile.getPathFromFolderName("/abcd/cs/"), "/abcd/cs/");
		assertEquals(DBSFile.getPathFromFolderName("abcd/cs/"), "/abcd/cs/");
		assertEquals(DBSFile.getPathFromFolderName("http://www.abcd.com"), "http://www.abcd.com/");
		assertEquals(DBSFile.getPathFromFolderName(""), "/");
		assertEquals(DBSFile.getPathFromFolderName("abcd//cs//"), "/abcd/cs/");
		assertEquals(DBSFile.getPathFromFolderName("//e"), "//e/");
		assertEquals(DBSFile.getPathFromFolderName("e//"), "/e/");
	}
	
	@Test
	public void testaFileNameFromPath(){
		assertEquals(DBSFile.getFileNameFromPath("http://www.abcd.com/cs"), "cs");
		assertEquals(DBSFile.getFileNameFromPath("http://www.abcd.com/cs/abcd"), "abcd");
		assertEquals(DBSFile.getFileNameFromPath("http://www.abcd.com/cs/abcd.txt"), "abcd.txt");
		assertEquals(DBSFile.getFileNameFromPath("/abcd/cs"), "cs");
		assertEquals(DBSFile.getFileNameFromPath("/abcd/cs/"), "");
		assertEquals(DBSFile.getFileNameFromPath("abcd/cs/"), "");
		assertEquals(DBSFile.getFileNameFromPath("abcd/cs"), "cs");
		assertEquals(DBSFile.getFileNameFromPath("abcd/add/cs.txt"), "cs.txt");
		assertEquals(DBSFile.getFileNameFromPath("abcd"), "abcd");
		assertEquals(DBSFile.getFileNameFromPath("http://www.abcd.com"), "www.abcd.com");
		assertEquals(DBSFile.getFileNameFromPath("http://www.abcd.com/"), "");
		assertEquals(DBSFile.getFileNameFromPath(""), "");
		assertEquals(DBSFile.getFileNameFromPath("abcd//cs//"), "");
		assertEquals(DBSFile.getFileNameFromPath("//e"), "e");
		assertEquals(DBSFile.getFileNameFromPath("e//"), "");
	}
	
//	@Test
	public void testaFilesFromPath(){
		DBSFile.getFilesFromPath("/Users/ricardo.villar", SORT_BY.MODIFIED_DATA, SORT_ORDER.ASCENDING);
		System.out.println("-----");
		DBSFile.getFilesFromPath("/Users/ricardo.villar", SORT_BY.MODIFIED_DATA, SORT_ORDER.DESCENDING);
		System.out.println("========================================");
		DBSFile.getFilesFromPath("/Users/ricardo.villar", SORT_BY.SIZE, SORT_ORDER.ASCENDING);
		System.out.println("-----");
		DBSFile.getFilesFromPath("/Users/ricardo.villar", SORT_BY.SIZE, SORT_ORDER.DESCENDING);
		System.out.println("========================================");
		DBSFile.getFilesFromPath("/Users/ricardo.villar", SORT_BY.NAME, SORT_ORDER.ASCENDING);
		System.out.println("-----");
		DBSFile.getFilesFromPath("/Users/ricardo.villar", SORT_BY.NAME, SORT_ORDER.DESCENDING);
	}
	
//	@Test
	public void testea(){
				//Busca na pasta local principal os subdiretórios onde foram efetuados cada download
		File[] xFiles = DBSFile.getFilesFromPath("/Users/ricardo.villar/ifeed/BCB");
		for (File xFile:xFiles){
			if (xFile.isDirectory() &&
				!xFile.isHidden()){
				System.out.println(xFile.getAbsolutePath());
			}
		}
	}

}
