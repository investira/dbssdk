package br.com.dbsoft.util;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.APP_SERVER_PROPERTY;

public class DBSFile {
	
	private static Logger		wLogger = Logger.getLogger(DBSFile.class);

	public static class FILE_EXTENSION{
		public static final String PDF = ".pdf";
		public static final String HTML = ".html";
		public static final String XML = ".xml";
		public static final String XLS = ".xls";
		public static final String JASPER= ".jasper";
		public static final String JRXML = ".jrxml";
		public static final String ZIP = ".zip";
		public static final String DOC = ".doc";
		public static final String RAR = ".rar";
	}
	
	public static class CONTENT_TYPE{
		public static final String PDF = "application/pdf";
		public static final String XML = "application/xml";
		public static final String XLS = "application/excel";
	}
	
	
	
	/**
	 * Retorna o nome simples do arquivo com a extensão do jasper
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameJASPER(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.JASPER);
	}
	
	/**
	 * Retorna o nome simples do arquivo com a extensão do jrxml
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameJRXML(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.JRXML);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão pdf
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNamePDF(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.PDF);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão xml
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameXML(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.XML);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão xls
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameXLS(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.XLS);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameHTML(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.HTML);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameZIP(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.ZIP);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameDOC(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.DOC);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameRAR(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE_EXTENSION.RAR);
	}
	
	/**
	 * Retorna se dois arquivo são iguais considerando a data da última modificação
	 * @param pFilePathA
	 * @param pFilePathB
	 * @return
	 */
	public static Boolean isEqual(String pFilePathA, String pFilePathB){
		File xFileA = new File(pFilePathA);
		File xFileB = new File(pFilePathA);
		if (xFileA.exists() &&
			xFileB.exists()){
			if (xFileA.lastModified() == xFileB.lastModified() &&
				xFileA.length() == xFileB.length()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna se dois arquivo são iguais considerando a data da última modificação
	 * @param pFilePathA
	 * @param pFilePathB
	 * @return
	 */
	public static Boolean isEqualDate(String pFilePathA, String pFilePathB){
		File xFileA = new File(pFilePathA);
		File xFileB = new File(pFilePathB);
		if (xFileA.exists() &&
			xFileB.exists()){
			if (xFileA.lastModified() == xFileB.lastModified()){
				return true;
			}
		}
		return false;
	}	

	/**
	 * Retorna se dois arquivo são iguais considerando a data da última modificação
	 * @param pFilePathA
	 * @param pFilePathB
	 * @return
	 */
	public static Boolean setLastModifiedDate(String pFilePath, Date pDateTime){
		return setLastModifiedDate(pFilePath, pDateTime);
	}	

	/**
	 * Retorna se dois arquivo são iguais considerando a data da última modificação
	 * @param pFilePathA
	 * @param pFilePathB
	 * @return
	 */
	public static Boolean setLastModifiedDate(String pFilePath, long pDateTime){
		File xFileA = new File(pFilePath);
		if (xFileA.exists()){
			xFileA.setLastModified(pDateTime);
		}
		return false;
	}	
	
	/**
	 * Copia a data de última modificação de um arquivo para o outro
	 * @param pFilePathFrom
	 * @param pFilePathTo
	 * @return
	 */
	public static Boolean copyLastModifiedData(String pFilePathFrom, String pFilePathTo){
		File xFileA = new File(pFilePathFrom);
		File xFileB = new File(pFilePathTo);
		if (xFileA.exists() &&
			xFileB.exists()){
			if (xFileA.lastModified() != xFileB.lastModified()){
				xFileB.setLastModified(xFileA.lastModified());
			}
		}
		return false;
	}

	/**
	 * Método de descompactação de arquivo
	 * 
	 * @param pSourceFile
	 *            : path completo do arquivo zip a ser descompactado.
	 * @param pDestinationFile
	 *            : path completo do DIRETÓRIO de destino.
	 * @return
	 * @throws IOException
	 */
	public static File unzip(String pFileZiped, String pDiretorio) {
		
		@SuppressWarnings("rawtypes")
		Enumeration xFileZipedElements;
		ZipFile xFileZiped = null;
		ZipEntry xFileZipedObject = null;
		try {
			xFileZiped = new ZipFile(pFileZiped);
			xFileZipedElements = xFileZiped.entries();
			while (xFileZipedElements.hasMoreElements()) {
				xFileZipedObject = (ZipEntry) xFileZipedElements.nextElement();
				if (xFileZipedObject.isDirectory()) {
					//System.err.println("Descompactando diretório: " + xFileZipedObject.getName());
					(new File(pDiretorio + xFileZipedObject.getName())).getName();
					continue;
				}
				//System.out.println("Descompactando arquivo:" + xFileZipedObject.getName());
				pvCopyInputStream(xFileZiped.getInputStream(xFileZipedObject), 
						new BufferedOutputStream(new FileOutputStream(pDiretorio + xFileZipedObject.getName())));

			}
		} catch (IOException e) {
			wLogger.error("Erro ao descompactar", e);
			return null;
		}finally{
			try {
				if (xFileZiped!=null){
					xFileZiped.close();
				}
			} catch (IOException e) {
				wLogger.error("Erro ao descompactar:close", e);
			}
		}
		return new File(pDiretorio + xFileZipedObject.getName());
	}
	

	
	/**
	 * Método de descompactação de arquivo
	 * 
	 * @param pSourceFile
	 *            : path completo do arquivo zip a ser descompactado.
	 * @param pDestinationFile
	 *            : path completo do DIRETÓRIO de destino.
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static boolean unzip_OLD(String pSourceFile, String pDestinationFile)
			throws IOException {
		final Path xDestDirPath = Paths.get(pDestinationFile);
		final Path xZipPath = Paths.get(pSourceFile);

		if (!DBSFile.exists(pSourceFile)) {
			return false;
		}

		if (Files.notExists(xDestDirPath)) {
			//System.out.println("Creating Directory: " + xDestDirPath);
			Files.createDirectories(xDestDirPath);
		}

		try (FileSystem xZipFileSystem = FileSystems.newFileSystem(xZipPath,
				null)) {
			
			final Path xRoot = xZipFileSystem.getPath("/");

			Files.walkFileTree(xRoot, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path pFile,
						BasicFileAttributes attrs) throws IOException {
					final Path xDestFile = Paths.get(xDestDirPath.toString(),
							pFile.toString());
					//System.out.printf("Extracting file %s to %s\n", pFile,
					//		xDestFile);
					Files.copy(pFile, xDestFile, COPY_ATTRIBUTES);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path pDirectory,
						BasicFileAttributes attrs) throws IOException {
					final Path xDirToCreate = Paths.get(
							xDestDirPath.toString(), pDirectory.toString());
					if (Files.notExists(xDirToCreate)) {
					//	System.out.printf("Creating directory %s\n",
					//			xDirToCreate);
						Files.createFile(xDirToCreate);
					}
					return FileVisitResult.CONTINUE;
				}
			});
			return true;
		} catch (Exception e) {
			wLogger.error(e);
			return false;
		}
	}

	/**
	 * Método de compactação de arquivos
	 * 
	 * @param pZipFilename
	 * @param pFilenames
	 * @return
	 * @throws IOException
	 */
	public static boolean zipFile(String pZipFilename, String... pFilenames){
		String[] xFileName = pFilenames;
		if (!DBSFile.exists(xFileName[0])) {
			return false;
		}
		try (FileSystem xZipFileSystem = pvCreateZipFileSystem(pZipFilename,
				true)) {
			final Path xRoot = xZipFileSystem.getPath("/");

			for (String xFilename : pFilenames) {
				final Path xSourcePath = Paths.get(xFilename);

				if (!Files.isDirectory(xSourcePath)) {
					final Path XDestinationPath = xZipFileSystem.getPath(
							xRoot.toString(), xSourcePath.toString());
					final Path xParentPath = XDestinationPath.getFileName();
					if (Files.notExists(xParentPath)) {
						//System.out.printf("Compress File %s\n", xParentPath);
						Files.copy(xSourcePath, xParentPath,
								StandardCopyOption.COPY_ATTRIBUTES);
						return true;
					}
				} else {
					Files.walkFileTree(xSourcePath,
							new SimpleFileVisitor<Path>() {
								@Override
								public FileVisitResult visitFile(
										Path pFileSource,
										BasicFileAttributes pAttrs)
										throws IOException {
									final Path xFileDestination = xZipFileSystem
											.getPath(xRoot.toString(),
													pFileSource.toString());
									Files.copy(pFileSource, xFileDestination,
											StandardCopyOption.REPLACE_EXISTING);
									return FileVisitResult.CONTINUE;
								}

								@Override
								public FileVisitResult preVisitDirectory(
										Path dir, BasicFileAttributes attrs)
										throws IOException {
									final Path xDirToCreate = xZipFileSystem
											.getPath(xRoot.toString(),
													dir.toString());
									System.out.println(xDirToCreate
											.getFileName());
									if (Files.notExists(xDirToCreate
											.getFileName())) {
										System.out.printf(
												"Creating directory %s\n",
												xDirToCreate);
										Files.createDirectories(xDirToCreate);
									}
									return FileVisitResult.CONTINUE;
								}
							});
				}
			}
			return true;
		} catch (Exception e) {
			wLogger.error(e);
			return false;
		}

	}


	/*
	// byte[] xReadBuffer = new byte[xFilename.length];
	//
	// try {
	// ZipOutputStream xOutputZip = new ZipOutputStream(
	// new FileOutputStream(pDestinationFile));
	//
	// for (int i = 0; i < xFilename.length; i++) {
	// FileInputStream xInput = new FileInputStream(pSourceFile);
	// xOutputZip.putNextEntry(new ZipEntry(pSourceFile));
	// int xFileLength;
	// while ((xFileLength = xInput.read(xReadBuffer)) > 0) {
	// xOutputZip.write(xReadBuffer, 0, xFileLength);
	// }
	//
	// xOutputZip.closeEntry();
	// xInput.close();
	// }
	//
	// xOutputZip.close();
	// return true;
	// } catch (IOException e) {
	// return false;
	// }
	// }*/

	/**
	 * Método de download de arquivos
	 * 
	 * @param pSourceURL
	 *            : URL de onde o arquivo será baixado.
	 * @param pDestinationPath
	 *            : path completo de destino do arquivo baixado.
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static boolean download(String pSourceURL, String pDestinationPath) {
		try {
			URL xWebSite = new URL(pSourceURL);
			ReadableByteChannel xByteChannel = Channels.newChannel(xWebSite
					.openStream());
			FileOutputStream xOutputStream;
			xOutputStream = new FileOutputStream(pDestinationPath);
			xOutputStream.getChannel().transferFrom(xByteChannel, 0, 1 << 24);
			return true;
		} catch (IOException e) {
			wLogger.error(e);
			return false;
		}
	}

	/**
	 * Método de Upload de arquivos
	 * 
	 * @param pDestionationURL
	 *            padrão ftp://user:password@host:port/filePathDestino;type=i
	 * @param pSourceFile
	 *            : path completo do arquivo a ser upado
	 * @return
	 */
	public static boolean upload(String pDestionationURL, String pSourceFile) {
		if (!DBSFile.exists(pSourceFile)) {
			return false;
		}
		StringBuffer xStringBuffer = new StringBuffer(pDestionationURL);
		BufferedInputStream xBufferedInput = null;
		BufferedOutputStream xBufferedOutput = null;
		try {
			URL xUrl = new URL(xStringBuffer.toString());
			URLConnection xUrlconnection = xUrl.openConnection();
			xBufferedOutput = new BufferedOutputStream(
					xUrlconnection.getOutputStream());
			xBufferedInput = new BufferedInputStream(new FileInputStream(
					pSourceFile));

			int xI;
			while ((xI = xBufferedInput.read()) != -1) {
				xBufferedOutput.write(xI);
			}
			xBufferedInput.close();
			xBufferedOutput.close();
			return true;
		} catch (Exception e) {
			wLogger.error(e);
			return false;
		}
	}

	/**
	 * Método para mover arquivos
	 * 
	 * @param pOld
	 *            : Path completo do arquivo a ser movido.
	 * @param pNew
	 *            : path completo de destino do arquivo.
	 * @return
	 */
	public static boolean move(String pOld, String pNew) {
		if (!DBSFile.exists(pOld)) {
			return false;
		}
		File xOldFile = new File(pOld);
		File xNewFile = new File(pNew);

		boolean xIsOk = xOldFile
				.renameTo(new File(xNewFile, xOldFile.getName()));
		if (xIsOk) {
			//System.out.println("Arquivo foi movido com sucesso");
			return true;
		} else {
			//System.out.println("Nao foi possivel mover o arquivo");
			return false;
		}
	}

	/**
	 * Método de cópia de arquivos
	 * 
	 * @param pFile
	 *            : path completo do arquivo a ser copiado
	 * @param pDestFile
	 *            : Path completo de destino do arquivo
	 * @return
	 * @throws IOException
	 */
	public static boolean copy(String pSourceFile, String pFileCopied) {
		if (!DBSFile.exists(pSourceFile)) {
			return false;
		}

		Path xPathSourceFile = Paths.get(pSourceFile);
		Path xPathFileCopied = Paths.get(pFileCopied);

		try {
			Files.copy(xPathSourceFile, xPathFileCopied, COPY_ATTRIBUTES);
			return true;
		} catch (IOException e) {
			wLogger.error(e);
			return false;
		}
	}

	/**
	 * Método para deletar arquivos
	 * 
	 * @param pFile
	 *            : path completo do arquivo a ser deletado
	 * @return
	 */
	public static boolean delete(String pFile) {
		if (!DBSFile.exists(pFile)) {
			return false;
		}
		File xFileToDelete = new File(pFile);

		boolean xIsOk = xFileToDelete.delete();

		if (xIsOk) {
			//System.out.println("Arquivo deletado");
			return true;
		} else {
			//System.out.println("Arquivo não deletado");
			return false;
		}
	}

	/**
	 * Retorna se arquivo existe.
	 * @param pFile path completo do arquivo.
	 * @return
	 */
	public static boolean exists(String pFile) {
		boolean xExists = false;
		try{
			File xFile = new File(pFile);
			xExists = xFile.exists();
		}catch(Exception e){
			xExists = false;
			wLogger.error(e);
		}
		return xExists;
	}

	/**
	 * Cria diretório
	 * @param pFile
	 * @return true=Ok, false=erro
	 */
	public static boolean mkDir(File pFile){
		try{
			if (pFile != null 
			 && pFile.getParent() != null){
				File xDir = new File(pFile.getParent());
				return xDir.mkdirs();
			}else{
				wLogger.error("Diretório a ser criado não foi informado.");
				return false;
			}
		}catch(Exception e){
			wLogger.error(e);
			return false;
		}
	}
	
//	/**
//	 * Retorna o caminho ja normalizado, corrigindo erros
//	 * @param pPath
//	 * @return
//	 */
//
//	public static String getPath(String pPath){
//		if (pPath != null){
//			try {
//				System.out.println(new URI(pPath).normalize().getRawPath());
//				System.out.println(new URI(pPath).normalize().getHost());
//				System.out.println(new URI(pPath).normalize().getPath());
//				System.out.println(new URI(pPath).normalize().getRawQuery());
//				System.out.println(new URI(pPath).normalize().getRawSchemeSpecificPart());
//				System.out.println(new URI(pPath).normalize().getScheme());
//				System.out.println(new URI(pPath).normalize().getSchemeSpecificPart());
//				
//				return new URI(pPath).normalize().getRawPath();
//			} catch (URISyntaxException e) {
//				return "";
//			}
//		}
//		return "";
//	}
	
	/**
	 * Retorna o caminho ja normalizado, corrigindo erros, excluido "/" do inicio(se houver) e incluido "/" ao final.<br/>
	 * Se for uma URL conténdo o caminho completo, como por exemplo <b>http://www.acme.com/abc</b>,
	 * a parte que representa o servidor(http://www.acme.com/) será excluida e o valor retornado será <b>abc/</b>  
	 * @param pPath
	 * @return
	 */

	public static String getPath(String pPath){
		if (pPath != null){
			try {
				String xPath = new URI(pPath).normalize().getPath();
				if (xPath.startsWith("/")){
					xPath = xPath.substring(1, xPath.length());
				}
				if (!xPath.endsWith("/")){
					xPath += "/";
				}
				return xPath;
			} catch (URISyntaxException e) {
				return "";
			}
		}
		return "";
	}
	
	/**
	 * Retorna caminho local que está o servidor
	 * @return
	 */
	public static String getServerPath(){
		return System.getProperty(APP_SERVER_PROPERTY.PATH.JBOSS);
	}

	// ===============================================================================================
	// Private
	// ===============================================================================================

	private static FileSystem pvCreateZipFileSystem(String pZipFilename,
			boolean create) throws IOException {
		final Path xPath = Paths.get(pZipFilename);
		final URI xUri = URI.create("jar:file:" + xPath.toUri().getPath());

		final Map<String, String> xEnv = new HashMap<>();
		if (create) {
			xEnv.put("create", "true");
		}
		return FileSystems.newFileSystem(xUri, xEnv);
	}
	
	/**
	 * Método privado de cópia de Strem
	 * 
	 * @param pInputStream
	 * @param pOutputStream
	 * @throws IOException
	 */
	private static final void pvCopyInputStream(InputStream pInputStream,
			OutputStream pOutputStream) throws IOException {
		byte[] xBuffer = new byte[1024];
		int xFileLentgh;
		while ((xFileLentgh = pInputStream.read(xBuffer)) >= 0) {
			pOutputStream.write(xBuffer, 0, xFileLentgh);
		}
		pInputStream.close();
		pOutputStream.close();
	}
	
	/**
	 * Retorna o nome simples do arquivo com a extensão informada
	 * @param pFileName
	 * @return
	 */
	private static String pvGetFileNameWithExtention(String pFileName, String pExtension){
		return pFileName + pExtension;
	}

}
