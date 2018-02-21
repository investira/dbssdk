package br.com.dbsoft.util;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
import java.sql.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.FILE;
import br.com.dbsoft.core.DBSSDK.SYSTEM_PROPERTY;

public class DBSFile {
	
	private static Logger		wLogger = Logger.getLogger(DBSFile.class);

	public static enum SORT_BY{
		NAME,
		SIZE,
		MODIFIED_DATA
	}
	
	public static enum SORT_ORDER{
		ASCENDING,
		DESCENDING
	}
	
	/**
	 * Retorna o nome simples do arquivo com a extensão do jasper
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameJASPER(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.JASPER);
	}
	
	/**
	 * Retorna o nome simples do arquivo com a extensão do jrxml
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameJRXML(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.JRXML);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão pdf
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNamePDF(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.PDF);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão xml
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameXML(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.XML);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão xls
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameXLS(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.XLS);
	}
	
	/**
	 * Retorna o nome simples do arquivo com a extensão xls
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameXLSX(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.XLSX);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameHTML(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.HTML);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameZIP(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.ZIP);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameDOC(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.DOC);
	}

	/**
	 * Retorna o nome simples do arquivo com a extensão html
	 * @param pReportFileName
	 * @return
	 */
	public static String getFileNameRAR(String pReportFileName){
		return pvGetFileNameWithExtention(pReportFileName, FILE.EXTENSION.RAR);
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
	 * Método de descompactação de arquivo na mesma pasta que se encontra o arquivo.
	 * @param pZipFileName path completo do arquivo zip a ser descompactado.
	 * @return Em caso de sucesso, retorna <b>File</b> do último arquivo contido no arquivo compactado
	 * @throws IOException
	 */
	public static File unzipFile(String pZipFileName) {
		return unzipFile(pZipFileName, null);
	}

	/**
	 * Método de descompactação de arquivo.
	 * @param pZipFileName path completo do arquivo zip a ser descompactado.
	 * @param pDestinationFolder Caminho completo do diretório de destino.<br/>
	 * Se for nulo, será considerado o próprio caminho do arquivo compactado.
	 * @return Em caso de sucesso, retorna <b>File</b> do último arquivo contido no arquivo compactado
	 * @throws IOException
	 */
	public static File unzipFile(String pZipFileName, String pDestinationFolder) {
		@SuppressWarnings("rawtypes")
		Enumeration xZipFileElements;
		ZipFile 	xZipFile = null;
		ZipEntry 	xZipFileObject = null;
		
		if (pZipFileName == null){
			return null;
		}
		
		File xFile = new File(pZipFileName);
		if (pDestinationFolder == null){
			pDestinationFolder = xFile.getParent();
			if (pDestinationFolder == null){
				wLogger.error("Pasta destino para descompatar " + pZipFileName + " não foi encontrada.");
				return null;
			}
		}
		if (xFile.exists()
		 && xFile.canRead()) {
			pDestinationFolder = DBSFile.getPathFromFolderName(pDestinationFolder);
			try {
				xZipFile = new ZipFile(pZipFileName);
				xZipFileElements = xZipFile.entries();
				while (xZipFileElements.hasMoreElements()) {
					xZipFileObject = (ZipEntry) xZipFileElements.nextElement();
					
					if (xZipFileObject.getName().toLowerCase().endsWith(FILE.EXTENSION.ZIP)) {
						pvCopyInputStream(xZipFile.getInputStream(xZipFileObject), 
								new BufferedOutputStream(new FileOutputStream(xFile.getParent() +File.separator+ xZipFileObject.getName())));
						
						xFile = new File(xFile.getParent() +File.separator+ xZipFileObject.getName());
						//Corrige a hora de criação
						xFile.setLastModified(xZipFileObject.getTime());
						
						//Descompacta o arquivo compactado
						xFile = unzipFile(xFile.getAbsolutePath(), pDestinationFolder);
					} else {
						pvCopyInputStream(xZipFile.getInputStream(xZipFileObject), 
								new BufferedOutputStream(new FileOutputStream(pDestinationFolder + xZipFileObject.getName())));
						
						xFile = new File(pDestinationFolder + xZipFileObject.getName());
						//Corrige a hora de criação
						xFile.setLastModified(xZipFileObject.getTime());
					}
					
				}
			} catch (IOException e) {
				wLogger.error("Erro ao descompactar arquivo " + pZipFileName, e);
				return null;
			}finally{
				try {
					if (xZipFile!=null){
						xZipFile.close();
					}
				} catch (IOException e) {
					wLogger.error("Erro ao descompactar:close", e);
				}
			}
//			xFile = new File(pDestinationFolder + xZipFileObject.getName());
		} else {
			pZipFileName = null;
			wLogger.error("unZip: não é possível ler arquivo " + pZipFileName + ".");
		}		
		
		return xFile;
	}
	

	
	/**
	 * Método de compactação de arquivos
	 * 
	 * @param pZipFileName
	 * @param pFileNames
	 * @return
	 * @throws IOException
	 */
	public static boolean zipFile(String pZipFileName, String... pFileNames){
		String[] xFileName = pFileNames;
		if (!DBSFile.exists(xFileName[0])) {
			return false;
		}
		try (FileSystem xZipFileSystem = pvCreateZipFileSystem(pZipFileName,
				true)) {
			final Path xRoot = xZipFileSystem.getPath(File.separator); //TODO TESTAR ISTO NO WINDOWS.

			for (String xFilename : pFileNames) {
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
		if (xOldFile.exists()
		&& !xNewFile.exists()){
			return xOldFile.renameTo(xNewFile);
		}else{
			wLogger.error("Move não pode ser executado, por destino já existir.");
			return false;
		}
	}

	/**
	 * Método de cópia de arquivos.<br/>
	 * Sobre-escreve se arquivo com mesmo nome existir.
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
			Files.copy(xPathSourceFile, xPathFileCopied, REPLACE_EXISTING, COPY_ATTRIBUTES);
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
		if (pFile==null){
			return false;
		}
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
	 * Retorna se camimho existe.
	 * @param pFile path completo do arquivo.
	 * @return
	 */
	public static boolean existsPath(String pFile) {
		return exists(getPathFromFileName(pFile, false));
	}

	/**
	 * Cria diretório a partir do caminho informado.<br/>
	 * @param pPath Caminho do diretório a ser criado. Caminho não deverá incluir nome de arquivo.
	 * @return true = Já existia ou foi criado com sucesso<br/>false = não existe e não foi criado.
	 */
	public static boolean mkDir(String pPath){
		if (pPath == null){
			return false;
		}
		try{
			if (!exists(pPath)){
				File xFile = new File(pPath);
				return xFile.mkdirs();
			}else{
				return true;
			}
		}catch(Exception e){
			wLogger.error(e);
			return false;
		}
	}
	
	/**
	 * Cria diretório a partir do cominho completo contendo o nome do arquivo.
	 * @param pFile
	 * @return true=Ok, false=erro
	 */
	public static boolean mkDir(File pFile){
		if (pFile == null){
			return false;
		}
		try{
			if (pFile.getParent() != null){
				return mkDir(pFile.getParent());
			}else{
				wLogger.error("Diretório a ser criado não foi informado.");
				return false;
			}
		}catch(Exception e){
			wLogger.error(e);
			return false;
		}
	}
	
	/**
	 * Retorna a data da última modificação do arquivo informado.
	 * @param pFile Arquivo a ser pesquisado
	 * @return Data da última modificação se arquivo existir.<br/>
	 * <i>null</i> Em caso de arquivo não encontrado ou erro.
	 */
	public static Date getFileDateModified(String pFile){
		if (pFile == null){
			return null;
		}
		try{
			File xFile = new File(pFile);
			return DBSDate.toDate(xFile.lastModified());
		}catch(Exception e){
			wLogger.error(e);
			return null;
		}	
	}
	
	/**
	 * Retorna a data da última modificação do arquivo informado.
	 * @param pFile Arquivo a ser pesquisado
	 * @return Data da última modificação se arquivo existir.<br/>
	 * <i>null</i> Em caso de arquivo não encontrado ou erro.
	 */
	public static Long getFileLastModified(String pFile){
		if (pFile == null){
			return null;
		}
		try{
			File xFile = new File(pFile);
			if (xFile.exists()){
				return xFile.lastModified();
			}
			return null;
		}catch(Exception e){
			wLogger.error(e);
			return null;
		}	
	}

	/**
	 * Retorna o caminho do arquivo, incluindo "/" ao final(se não houver) e ao inicio quando não tiver sido informado o <b>procotolo</b>.<br/>
	 * Exclui a parte do caminho após o último "/".<br/>
	 * Considera que <b>pFileName</b> pode conter além do caminho, o nome de arquivo.
	 * <b>pFileName</b> terminado em "/" será considerado integralmente um diretório.<p>
	 * <b>pFileName</b> não terminado em "/", terá o conteúdo após a última "/" desconsiderado.<p> 
	 * Exemplos:<br/>
	 * <b>http://www.acme.com/abc/</b> retornará <b>http://www.acme.com/abc/</b>.<br/>
	 * <b>http://www.acme.com/abc</b> retornará <b>http://www.acme.com/</b>.<br/>
	 * <b>http://www.acme.com/</b> retornará <b>http://www.acme.com/</b>.<br/>
	 * <b>acme/</b> retornará <b>/acme/</b>.<br/>
	 * <b>acme</b> retornará <b>/</b>.<br/>
	 * <b>/acme/</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/abc</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/abc.txt</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/abc/xyz</b> retornará <b>/acme/abc/</b>.<br/>
	 * @param pFileName
	 * @return
	 */
	public static String getPathFromFileName(String pFileName){
		return pvGetPath(pFileName, true);
	}

	/**
	 * Retorna o caminho do arquivo, incluindo "/" ao final ou não conforme parametro <b>pAddSeparator</b> e ao inicio quando não tiver sido informado o <b>procotolo</b>.<br/>
	 * Exclui a parte do caminho após o último "/".<br/>
	 * Considera que <b>pFileName</b> pode conter além do caminho, o nome de arquivo.
	 * <b>pFileName</b> terminado em "/" será considerado integralmente um diretório.<p>
	 * <b>pFileName</b> não terminado em "/", terá o conteúdo após a última "/" desconsiderado.<p> 
	 * Exemplos:<br/>
	 * <b>http://www.acme.com/abc/</b> retornará <b>http://www.acme.com/abc/</b>.<br/>
	 * <b>http://www.acme.com/abc</b> retornará <b>http://www.acme.com/</b>.<br/>
	 * <b>http://www.acme.com/</b> retornará <b>http://www.acme.com/</b>.<br/>
	 * <b>acme/</b> retornará <b>/acme/</b>.<br/>
	 * <b>acme</b> retornará <b>/</b>.<br/>
	 * <b>/acme/</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/abc</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/abc.txt</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/abc/xyz</b> retornará <b>/acme/abc/</b>.<br/>
	 * @param pFileName
	 * @return
	 */
	public static String getPathFromFileName(String pFileName, boolean pAddSeparator){
		return pvGetPath(pFileName, true, pAddSeparator);
	}
	
	/**
	 * Retorna o caminho do arquivo, incluindo "/" ao final(se não houver) e ao inicio quando não tiver sido informado o <b>procotolo</b>.<br/>
	 * Considera que <b>pFolderName</b> é efetivamente um caminho(diretório) e não contém nome de arquivo.
	 * Exemplos:<br/>
	 * <b>http://www.acme.com/abc/</b> retornará <b>http://www.acme.com/abc/</b>.<br/>
	 * <b>http://www.acme.com/abc</b> retornará <b>http://www.acme.com/abc/</b>.<br/>
	 * <b>http://www.acme.com/</b> retornará <b>http://www.acme.com/</b>.<br/>
	 * <b>acme/</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/</b> retornará <b>/acme/</b>.<br/>
	 * <b>/acme/abc</b> retornará <b>/acme/abc/</b>.<br/>
	 * <b>/acme/abc.txt</b> retornará <b>/acme/abc.txt/</b>.<br/>
	 * <b>/acme/abc/xyz</b> retornará <b>/acme/abc/xyz/</b>.<br/>
	 * @param pFolderName
	 * @return
	 */
	public static String getPathFromFolderName(String pFolderName){
		return pvGetPath(pFolderName, false);
	}

	/**
	 * Retorna caminho local que está o servidor
	 * @return
	 */
	public static String getServerLocalPath(){
		return System.getProperty(SYSTEM_PROPERTY.SERVER_BASE_DIR);
	}
	
	/**
	 * Retorna caminho da aplicação
	 * @return
	 */
	public static String getAppLocalPath(){
		 return System.getProperty("user.dir");
	}

	/**
	 * Retorna somente o nome do arquivo a partir da caminho informado.<p>
	 * Exemplos:<br/>
	 * <b>http://www.acme.com/abc</b> retornará <b>abc/</b>.<br/>
	 * <b>http://www.acme.com/abc/</b> retornará <b>vázio</b>.<br/>
	 * <b>acme/</b> retornará <b>vázio</b>.<br/>
	 * <b>acme</b> retornará <b>acme</b>.<br/>
	 * <b>/acme/</b> retornará <b>vázio</b>.<br/>
	 * <b>/acme/abc</b> retornará <b>abc</b>.<br/>
	 * <b>/acme/abc.txt</b> retornará <b>abc.txt</b>.<br/>
	 * @param pPath
	 * @return
	 */
	public static String getFileNameFromPath(String pPath){
		if (pPath == null){return "";}
		int xI = pPath.lastIndexOf(File.separator);
		if (xI == -1){
			return pPath;
		}else{
			return DBSString.getSubString(pPath, xI + 2, pPath.length());
		}
	}

	/**
	 * Retorna lista de arquivos/diretórios existentes no caminho informado em <b>pPath</b> 
	 * por ordem crescente de nome.<br>
	 * @param pPath Caminho a ser pesquisado
	 * @return
	 */
	public static File[] getFilesFromPath(String pPath){
		return getFilesFromPath(pPath, SORT_BY.NAME, SORT_ORDER.ASCENDING);
	}

	/**
	 * Retorna lista de arquivos/distórios existentes no caminho informado em <b>ppath</b>.<br>
	 * @param pPath Caminho a ser pesquisado
	 * @param pSortBy Campo a ser considerado para efeito do ordenamento da lista
	 * @param pSortOrder Ordem da lista
	 * @return
	 */
	public static File[] getFilesFromPath(String pPath, SORT_BY pSortBy, SORT_ORDER pSortOrder){
		if (pPath == null){return null;}
		try{
			pPath = getPathFromFolderName(pPath);
			File 	xFile = new File(pPath);
			File[] 	xFiles = null;
			if (!xFile.exists()){
				System.out.println("Caminho " + pPath + " inexistente.");
				wLogger.error("Caminho " + pPath + " inexistente.");
				return null;
			}
			if (!xFile.isDirectory()){
				System.out.println("Caminho " + pPath + " não é um pasta.");
				wLogger.error("Caminho " + pPath + " não é um pasta.");
				return null;
			}
			xFiles = xFile.listFiles();
			
	    	if (pSortBy == SORT_BY.MODIFIED_DATA){
	    		if (pSortOrder == SORT_ORDER.ASCENDING){
					Arrays.sort(xFiles, new Comparator<File>(){
					    @Override
						public int compare(File pFile1, File pFile2){
				    		return Long.valueOf(pFile1.lastModified()).compareTo(pFile2.lastModified());
					    } 
					});
	    		}else{
					Arrays.sort(xFiles, new Comparator<File>(){
					    @Override
						public int compare(File pFile1, File pFile2){
				    		return -Long.valueOf(pFile1.lastModified()).compareTo(pFile2.lastModified());
					    } 
					});
	    		}
	    	}else if (pSortBy == SORT_BY.NAME){
	    		if (pSortOrder == SORT_ORDER.ASCENDING){
					Arrays.sort(xFiles, new Comparator<File>(){
					    @Override
						public int compare(File pFile1, File pFile2){
					        return String.CASE_INSENSITIVE_ORDER.compare(pFile1.getName(), pFile2.getName());
	
					    } 
					});
	    		}else{
					Arrays.sort(xFiles, new Comparator<File>(){
					    @Override
						public int compare(File pFile1, File pFile2){
					        return -String.CASE_INSENSITIVE_ORDER.compare(pFile1.getName(), pFile2.getName());
	
					    } 
					});
	    		}
	    	}else if (pSortBy == SORT_BY.SIZE){
	    		if (pSortOrder == SORT_ORDER.ASCENDING){
					Arrays.sort(xFiles, new Comparator<File>(){
					    @Override
						public int compare(File pFile1, File pFile2){
					        return Long.valueOf(pFile1.length()).compareTo(pFile2.length());
	
					    } 
					});
	    		}else{
					Arrays.sort(xFiles, new Comparator<File>(){
					    @Override
						public int compare(File pFile1, File pFile2){
					        return -Long.valueOf(pFile1.length()).compareTo(pFile2.length());
	
					    } 
					});
	    		}
	    	}

			return xFiles;
		}catch(Exception e){
			wLogger.error(e);
			return null;
		}
	}
	
	public static void displayFiles(File[] files) {
		for (File file : files) {
			System.out.printf("File: %-20s Last Modified:" + new Date(file.lastModified()) + " size:" + file.length() + "\n", file.getName());
		}
	}

	/**
	 * Retorna o caminho completo contendo o nome do arquivo, corrigindo eventuais problemas de barras "/".
	 * @param pPath
	 * @param pFile
	 * @return
	 */
	public static String getPathNormalized(String pPath, String pFile){
		if (pPath == null){pPath = "";}
		if (pFile == null){pFile = "";}
		pPath = pPath.trim();
		if (!pPath.endsWith(File.separator)){
			pPath += File.separator;
		}
		pFile = pFile.trim();
		if (pFile.startsWith(File.separator)){
			pFile = pFile.substring(1, pFile.length());
		}
		return pPath + pFile;
	}
	
	/**
	 * Retorna quantidade de arquivos dentro da lista de pastas informadas.<br/>
	 * Utiliza <b>pFileExtension</b> como filtro dos arquivos que serão considerados.
	 * Caso não seja informado, irá considerar todos arquivos.<br/>
	 * Só serão considerados arquivos visíveis.
	 * @param pFolders
	 * @param pFileExtension
	 * @return
	 */
	public static Integer getFilesCount(List<File> pFolders, String pFileExtension){
		Integer xCount = 0;
		for (File xFolder:pFolders){
			for (File xFile:xFolder.listFiles()){
				if (xFile.isFile()
				&& !xFile.isHidden()){
					if (DBSObject.isEmpty(pFileExtension) 
					 || xFile.getName().toLowerCase().endsWith(pFileExtension)){
						xCount ++;
					}
				}
			}
		}
		return xCount;
	}

	
	// ===============================================================================================
	// Private
	// ===============================================================================================

	/**
	 * Retorna o caminho do diretório.
	 * @param pName
	 * @param pHasFile true: Indica se <b>pNome</b> pode ser conter um nome de arquivo.<p>
	 * false: Indica se <b>pNome</b> contém somente diretório, independentemente se termina ou não com "/"
	 * @return
	 */
	private static String pvGetPath(String pName, boolean pHasFile){
		return pvGetPath(pName, pHasFile, true);
	}	
	
	/**
	 * Retorna o caminho do diretório.
	 * @param pName
	 * @param pHasFile true: Indica se <b>pNome</b> pode ser conter um nome de arquivo.<p>
	 * false: Indica se <b>pNome</b> contém somente diretório, independentemente se termina ou não com "/"
	 * @return
	 */
	private static String pvGetPath(String pName, boolean pHasFile, boolean pAddSeparator){
		if (pName != null){
			try {
				URI xURI = new URI(pName).normalize();
				String xHost = "";
				String xPath = xURI.getPath();
				//Procotolo
				if (xURI.getScheme()!=null){
					xHost = xURI.getScheme() + ":";
				}
				//Servidor
				if (xURI.getHost()!=null){
					xHost += "//" + xURI.getHost();
				}
				//Inclui barra inicial
				if (xURI.getHost()==null
				&& !xPath.startsWith(File.separator)){
					xPath = File.separator + xPath; 
				}
				//Encontra diretório pai, se houver.
				if (pHasFile){
					if (!xPath.endsWith(File.separator)){
						File xFile = new File(xPath);
						xPath = DBSObject.getNotNull(xFile.getParent(),""); 
					}
				}
				if (pAddSeparator){
					//Inclui barra final
					if (!xPath.endsWith(File.separator)){
						xPath += File.separator;
					}
				}else{
					//Exclui
					if (xPath.endsWith(File.separator)){
						xPath = xPath.substring(0, xPath.length());
					}
				}
				return xHost + xPath;
			} catch (URISyntaxException e) {
				wLogger.error(e);
				return "";
			}
		}
		return "";
	}
	
	
	
	private static FileSystem pvCreateZipFileSystem(String pZipFilename, boolean pCreate) throws IOException {
		final Path xPath = Paths.get(pZipFilename);
		final URI xUri = URI.create("jar:file:" + xPath.toUri().getPath());

		final Map<String, String> xEnv = new HashMap<>();
		if (pCreate) {
			xEnv.put("create", "true");
		}
		return FileSystems.newFileSystem(xUri, xEnv);
	}
	
	/**
	 * Método privado de cópia de Stream
	 * 
	 * @param pInputStream
	 * @param pOutputStream
	 * @throws IOException
	 */
	private static final void pvCopyInputStream(InputStream pInputStream, OutputStream pOutputStream) throws IOException {
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
