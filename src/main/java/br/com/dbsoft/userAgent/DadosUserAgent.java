package br.com.dbsoft.userAgent;

import br.com.dbsoft.util.DBSSys.SYS.DEVICE;
import br.com.dbsoft.util.DBSSys.SYS.OS;
import br.com.dbsoft.util.DBSSys.SYS.WEB_CLIENT;

public class DadosUserAgent {

	private String 		wRawUserAgent;
	private OS			wOS;
	private DEVICE		wDevice;
	private WEB_CLIENT	wBrowser;
	private String		wBrowserVersion;
	
	public String getRawUserAgent() {
		return wRawUserAgent;
	}
	public void setRawUserAgent(String pRawUserAgent) {
		wRawUserAgent = pRawUserAgent;
	}

	public OS getOS() {
		return wOS;
	}
	public void setOS(OS pOS) {
		wOS = pOS;
	}

	public DEVICE getDevice() {
		return wDevice;
	}
	public void setDevice(DEVICE pDevice) {
		wDevice = pDevice;
	}

	public WEB_CLIENT getBrowser() {
		return wBrowser;
	}
	public void setBrowser(WEB_CLIENT pBrowser) {
		wBrowser = pBrowser;
	}

	public String getBrowserVersion() {
		return wBrowserVersion;
	}
	public void setBrowserVersion(String pBrowserVersion) {
		wBrowserVersion = pBrowserVersion;
	}
	
}
