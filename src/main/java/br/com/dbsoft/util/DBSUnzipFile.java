package br.com.dbsoft.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DBSUnzipFile {
	private ZipEntry wFileZipedObject;

	/**
	 * Descompacta arquivo a partir do path do arquivo zipado
	 * 
	 * @param pFileZiped
	 * @return
	 */
	public File unzipFile(ZipFile pFileZiped, String pDiretorio) {
		@SuppressWarnings("rawtypes")
		Enumeration xFileZipedElements;
		//"/home/jose_addario/";
		try {
			xFileZipedElements = pFileZiped.entries();
			while (xFileZipedElements.hasMoreElements()) {
				wFileZipedObject = (ZipEntry) xFileZipedElements.nextElement();
				if (wFileZipedObject.isDirectory()) {
					System.err.println("Descompactando diretório: "
							+ wFileZipedObject.getName());
					(new File(pDiretorio + wFileZipedObject.getName())).getName();
					continue;
				}
				System.out.println("Descompactando arquivo:"
						+ wFileZipedObject.getName());
				pvCopyInputStream(pFileZiped.getInputStream(wFileZipedObject),
						new BufferedOutputStream(new FileOutputStream(
								pDiretorio + wFileZipedObject.getName())));

			}

		} catch (IOException ioexception) {
			System.err.println("Erro ao descompactar:"
					+ ioexception.getMessage());
		}
		return new File(pDiretorio + wFileZipedObject.getName());
	}

	private static final void pvCopyInputStream(InputStream pInputStream,
			OutputStream pOutputStream) throws IOException {
		byte[] xBuffer = new byte[1024];
		int xFileLenght;
		while ((xFileLenght = pInputStream.read(xBuffer)) >= 0) {
			pOutputStream.write(xBuffer, 0, xFileLenght);
		}
		pInputStream.close();
		pOutputStream.close();
	}

}