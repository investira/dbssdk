package br.com.dbsoft.io;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import br.com.dbsoft.file.DBSFileTransfer;

public class TstFileTransfer {

	
	//@Test
	public void test_File() {
		//List<Path> xSourceFiles = new ArrayList<Path>();
		Path xPath;
		//xPath = Paths.get("/Users/ricardovillar/downloads/cvm");
		//xPath = Paths.get("/Users/ricardovillar/downloads/cvm/empresas.txt");
		xPath = Paths.get("ftp://usuario:senha@www.dbsoft.com.br:21/tmp");
		
		System.out.println("toAbsolutePath:\t" +  xPath.toAbsolutePath().toString());
		System.out.println("getNameCount:\t" +  xPath.getNameCount());
		System.out.println("getFileName:\t" +  xPath.getFileName());
		System.out.println("getParent:\t" +  xPath.getParent());
		System.out.println("getRoot:\t" +  xPath.getRoot());
		System.out.println("normalize:\t" +  xPath.normalize().toString());
		System.out.println("toFile().getFreeSpace:\t" +  xPath.toFile().getFreeSpace());
		System.out.println("toFile().getTotalSpace:\t" +  xPath.toFile().getTotalSpace());
		System.out.println("toFile().getUsableSpace:\t" +  xPath.toFile().getUsableSpace());
		System.out.println("toFile().length\t" +  xPath.toFile().length());
		System.out.println("toUri()\t" +  xPath.toUri().toString());
		
		//ftp://usuario:senha@www.dbsoft.com.br:21/tmp;type=i
		
	}
	
	@Test
	public void test_Atributos() throws IOException {
		
//		System.out.println("CIAS ABERTAS");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_CIA_ABERTA.ZIP");
		
//		System.out.println("Fnd Investimentos");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_FI.ZIP");
//		
		System.out.println("Auditoria");
		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_AUDIT.ZIP");
//		
//		System.out.println("ADM CONS");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_ADM_CONS.ZIP");
//		
//		System.out.println("Demais Participantes");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_DEMAIS_PARTIC.ZIP");
//		
//		System.out.println("Inv. Estrangeiros");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_INV_ESTRANG.ZIP");
//		
//		System.out.println("CIA Incent.");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_CIA_INCENT.ZIP");
//		
//		System.out.println("Representante");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_REPRES.ZIP");
//		
//		System.out.println("ADM FDO Imobiliario");
//		getHttpAtr("http://www.cvm.gov.br/cadastro/SPW_ADM_FDO_IMOB.ZIP");
//		
//		System.out.println("ISIN BMF 2012-9-18");
//		getHttpAtr("http://www.bmfbovespa.com.br/isin/DownloadArquivo.asp?TipoArquivo=D&DataCria=2012-9-18%208:24:59");
//		
//		System.out.println("ISIN BMF 2012-9-15");
//		getHttpAtr("http://www.bmfbovespa.com.br/isin/DownloadArquivo.asp?TipoArquivo=D&DataCria=2012-9-15%208:24:46");
//		
//		System.out.println("ISIN BMF 2012-9-14");
//		getHttpAtr("http://www.bmfbovespa.com.br/isin/DownloadArquivo.asp?TipoArquivo=D&DataCria=2012-9-14%208:24:46");
	}
	
	private void getHttpAtr(String pURL) throws IOException {
		URL xURL = new URL(pURL);
		HttpURLConnection xConnection = (HttpURLConnection) xURL.openConnection();
		xConnection.setRequestProperty("Request-Method", "GET");
		xConnection.setDoInput(true);
		xConnection.setDoOutput(false);
		xConnection.connect();
		
		System.out.println("Data: " + new Timestamp(xConnection.getDate()));
		System.out.println("Tamanho: " + xConnection.getContentLength());
		System.out.println("Tamanho Long: " + xConnection.getContentLengthLong());
		System.out.println("Tipo: " + xConnection.getContentType());
		System.out.println("Modificado desde: " + new Timestamp(xConnection.getIfModifiedSince()));
		System.out.println("Ultima Modificacao: " + new Timestamp(xConnection.getLastModified()));
		System.out.println("Codigo de Resposta: " + xConnection.getResponseCode());
		System.out.println("Menssagem de Resposta: " + xConnection.getResponseMessage() + "\n");
		
		xConnection.disconnect();
	}
	
	@Test
	public void test_Download() throws ParseException {
		System.out.println("CIAS ABERTAS");
//		SPW_CIA_ABERTA - SPW_FI - SPW_AUDIT - SPW_ADM_CONS - SPW_DEMAIS_PARTIC
//		SPW_INV_ESTRANG - SPW_CIA_INCENT - SPW_REPRES - SPW_ADM_FDO_IMOB
		
		String xUrl = "http://www.cvm.gov.br/cadastro/SPW_AUDIT.ZIP";
		DBSFileTransfer xFileTransfer = new DBSFileTransfer(xUrl, "C:/FNT/Arquivos_Integracao/Teste/CVM_TESTE.zip", null);
		
		SimpleDateFormat xFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp xVersao = new Timestamp(xFormat.parse("2012-09-18 07:01:19.0").getTime());
		xFileTransfer.setVersion(xVersao);
		
		File xFile = xFileTransfer.transfer();
		System.out.println("Data da ultima Modificação: " + new Timestamp(xFile.lastModified()));
		System.out.println("Tamanho: " + xFile.length() + " bytes");
		System.out.println("Arquivo " + xFile.getAbsolutePath() + " baixado!");
	}
}
