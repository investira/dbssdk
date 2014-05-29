package br.com.dbsoft.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TstZip {
	public static void main(String[] args) {

		String[] nomesDosArquivos = new String[] { "/home/jose_addario/TesteZip" };

		byte[] bufferDeLeitura = new byte[1024];

		try {
			String nomeDoArquivosDeSaida = "/home/jose_addario/DestinoTeste/arquivoZipado.zip";
			ZipOutputStream saidaZip = new ZipOutputStream(
					new FileOutputStream(nomeDoArquivosDeSaida));

			for (int i = 0; i < nomesDosArquivos.length; i++) {
				FileInputStream in = new FileInputStream(nomesDosArquivos[i]);
				saidaZip.putNextEntry(new ZipEntry(nomesDosArquivos[i]));
				int len;
				while ((len = in.read(bufferDeLeitura)) > 0) {
					saidaZip.write(bufferDeLeitura, 0, len);
				}

				saidaZip.closeEntry();
				in.close();
			}

			saidaZip.close();
		} catch (IOException e) {
		}
	}
}
