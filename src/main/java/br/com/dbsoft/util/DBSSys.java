package br.com.dbsoft.util;

import br.com.dbsoft.userAgent.DadosUserAgent;
import br.com.dbsoft.util.DBSSys.SYS.DEVICE;
import br.com.dbsoft.util.DBSSys.SYS.OS;
import br.com.dbsoft.util.DBSSys.SYS.WEB_CLIENT;

public class DBSSys {

	public static DadosUserAgent getUserAgent(String pRawUserAgent) {
		if (DBSObject.isNull(pRawUserAgent)) {
			return null;
		}
		DadosUserAgent xUserAgent = new DadosUserAgent();
		
		xUserAgent.setRawUserAgent(pRawUserAgent);
		xUserAgent.setOS(OS.getOSFromUserAgent(pRawUserAgent));
		xUserAgent.setDevice(DEVICE.getDeviceFromUserAgent(pRawUserAgent));
		xUserAgent.setBrowser(WEB_CLIENT.getBrowserFromUserAgent(pRawUserAgent));
		xUserAgent.setBrowserVersion(WEB_CLIENT.getBrowserVersionFromUserAgent(pRawUserAgent));
		return xUserAgent;
	}
	
	public static final class SYS {
		public enum APP_SERVER { //Aplication Server
			JBOSS,
			WEBSPHERE,
			GLASSFISH,
			WILDFLY;
		}
		
		public enum OS {
			UNIX			("Unix OS Based", 			"x11"),
			ANDROID 		("Android OS", 				"android"),
			BADA			("Bada", 					"bada"),
			BEOS			("Beos", 					"beos"),
			BLACKBERRY_OS	("Blackberry OS", 			"blackberry, bb"),
			CHROME_OS		("Chrome OS", 				"cros"), //X11
			DARWIN			("Darwin", 					"darwin"),
			FIRE_OS			("Fire OS", 				"kindle fire, kf"),
			FREE_BSD		("FreeBSD", 				"freebsd"),
			HAIKU			("Haiku", 					"haiku"),
			HP_WEBOS		("HP webOS", 				"hpwos"),
			IOS 			("iOS", 					"iphone, ipod, ipad"),
			IRIX			("Irix", 					"irix"),			
			LINUX 			("Linux OS", 				"linux"),
			LIVEAREA		("Livearea", 				"playstation vita"),
			MAC_OS 			("Mac OS", 					"mac, macos x "),
			OPEN_BSD		("OpenBSD", 				"openbsd"),
			RIM_OS 			("RIM", 					"rim tablet os"),
			SUNOS			("SunOS", 					"sunos"),
			SYMBIAN 		("Symbian", 				"symbianos"),
			WEBOS 			("webOS", 					"webos"),
			WINDOWS 		("Microsoft Windows", 		"windows"),
			WINDOWSPHONE 	("Microsoft WindowsPhone", 	"windows phone");
			
			private String 	wName;
			private String 	wUserAgent;
			public String 	getName() {return wName;}
			public String 	getUserAgent() {return wUserAgent;}
			
			private OS(String pName, String pUserAgent) {
				wName = pName;
				wUserAgent = pUserAgent;
			}
			
			public static OS get(Integer pCode) {
				if (DBSObject.isNull(pCode)) {
					return null;
				}
				switch (pCode) {
					case 0:
						return UNIX;
					case 1:
						return ANDROID;
					case 2:
						return BADA;
					case 3:
						return BEOS;
					case 4:
						return BLACKBERRY_OS;
					case 5:
						return CHROME_OS;
					case 6:
						return DARWIN;
					case 7:
						return FIRE_OS;
					case 8:
						return FREE_BSD;
					case 9:
						return HAIKU;
					case 10:
						return HP_WEBOS;
					case 11:
						return IOS;
					case 12:
						return IRIX;		
					case 13:
						return LINUX;
					case 14:
						return LIVEAREA;
					case 15:
						return MAC_OS;
					case 16:
						return OPEN_BSD;
					case 17:
						return RIM_OS;
					case 18:
						return SUNOS;
					case 19:
						return SYMBIAN;
					case 20:
						return WEBOS;
					case 21:
						return WINDOWS;
					case 22:
						return WINDOWSPHONE;
					default:
						return null;
				}
			}
			
			public static OS getOSFromUserAgent(String pUserAgent) {
				OS xOS = null;
				String xUserAgent = pUserAgent.toLowerCase();
				if (DBSString.contains(xUserAgent, DBSString.toArrayList(BADA.wUserAgent, ","))) {
					return BADA;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(BEOS.wUserAgent, ","))) {
					return BEOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(BLACKBERRY_OS.wUserAgent, ","))) {
					return BLACKBERRY_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(CHROME_OS.wUserAgent, ","))) {
					return CHROME_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(DARWIN.wUserAgent, ","))) {
					return DARWIN;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(FIRE_OS.wUserAgent, ","))) {
					return FIRE_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(FREE_BSD.wUserAgent, ","))) {
					return FREE_BSD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(HAIKU.wUserAgent, ","))) {
					return HAIKU;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(HP_WEBOS.wUserAgent, ","))) {
					return HP_WEBOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IRIX.wUserAgent, ","))) {
					return IRIX;		
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(LIVEAREA.wUserAgent, ","))) {
					return LIVEAREA;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(OPEN_BSD.wUserAgent, ","))) {
					return OPEN_BSD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(RIM_OS.wUserAgent, ","))) {
					return RIM_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(SUNOS.wUserAgent, ","))) {
					return SUNOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(SYMBIAN.wUserAgent, ","))) {
					return SYMBIAN;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WEBOS.wUserAgent, ","))) {
					return WEBOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWSPHONE.wUserAgent, ","))) {
					return WINDOWSPHONE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWS.wUserAgent, ","))) {
					return WINDOWS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IOS.wUserAgent, ","))) {
					return IOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(MAC_OS.wUserAgent, ","))) {
					return MAC_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(ANDROID.wUserAgent, ","))) {
					return ANDROID;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(LINUX.wUserAgent, ","))) {
					return LINUX;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(UNIX.wUserAgent, ","))) {
					return UNIX;
				}
				return xOS;
			}
		}

		public enum APP_CLIENT {
			WEB,
			DOTNET,
			OBJC,
			JAVA;
		}

		public static enum WEB_CLIENT {
			DEFAULT		("Defaut", ""),
			CHROME		("Chrome", "-webkit-"),
			FIREFOX		("Mozilla", "-moz-"),
			OPERA		("Opera", "-o-"),
			SAFARI		("Safari", "-webkit-"),
			MICROSOFT	("Microsoft", "-ms-");
		
			private String 	wCSSPrefix;
			private String 	wUserAgent;
			public String getCSSPrefix() {return wCSSPrefix;}
			public String getUserAgent() {return wUserAgent;}
			
			private WEB_CLIENT(String pUserAgent, String pCSSPrefix) {
				wUserAgent = pUserAgent;
				wCSSPrefix = pCSSPrefix;
			}

			public static WEB_CLIENT get(String pCSSPrefix) {
				switch (pCSSPrefix) {
				case "-webkit-":
					return CHROME;
				case "-moz-":
					return FIREFOX;
				case "-o-":
					return OPERA;
				case "-ms-":
					return MICROSOFT;
				default:
					return DEFAULT;
				}
			}
			
			public static WEB_CLIENT get(Integer pCode) {
				if (DBSObject.isNull(pCode)) {
					return null;
				}
				switch (pCode) {
				case 1:
					return CHROME;
				case 2:
					return FIREFOX;
				case 3:
					return OPERA;
				case 4:
					return SAFARI;
				case 5:
					return MICROSOFT;
				default:
					return DEFAULT;
				}
			}
			
			public static WEB_CLIENT getBrowserFromUserAgent(String pUserAgent) {
				WEB_CLIENT xBrowser = WEB_CLIENT.DEFAULT; //Desconhecido
				if (pUserAgent.contains("MSIE")) {
				    xBrowser = WEB_CLIENT.MICROSOFT;
				} else if (pUserAgent.contains("Chrome")) {
					xBrowser = WEB_CLIENT.CHROME;
				} else if (pUserAgent.contains("Firefox")) {
					xBrowser = WEB_CLIENT.FIREFOX;
				} else if (pUserAgent.contains("Safari") && !pUserAgent.contains("Chrome")) {
					xBrowser = WEB_CLIENT.SAFARI;
				} else if (pUserAgent.contains("Opera")) {
					xBrowser = WEB_CLIENT.OPERA;
				}
				return xBrowser;
			}
			
			public static String getBrowserVersionFromUserAgent(String pUserAgent) {
				if (DBSObject.isEmpty(pUserAgent)) {return "";}
				String xBrowserVer = pUserAgent;
				if (xBrowserVer.contains("Chrome")) {
					//Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf("Chrome"), xBrowserVer.lastIndexOf(" "));
				} else if (xBrowserVer.contains("Firefox")) {
					//Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf(" ")+1);
				} else if (xBrowserVer.contains("Safari") && xBrowserVer.contains("Version")) {
					//Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/604.5.6 (KHTML, like Gecko) Version/11.0.3 Safari/604.5.6
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf(")")+2, xBrowserVer.lastIndexOf(" "));
				} else {
					//iPhone //Mozilla/5.0 (iPhone; CPU iPhone OS 11_2 like Mac OS X) AppleWebKit/604.4.7 (KHTML, like Gecko) Mobile/15C107
					//Android //Mozilla/5.0 (Linux; Android 7.1.1; Moto Z2 Play Build/NPSS26.118-19-1-6; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/65.0.3325.109 Mobile Safari/537.36
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf(")")+2);
				}
				return xBrowserVer;
			}
		}

		public enum DEVICE {
			UNKNOW 				("Unknow", 				"", 										false),
			MACINTOSH 			("Mac", 				"macintosh", 								false),
			TV_LG				("TV LG", 				"webos", 									false),
			TV					("TV", 					"smart-tv, smarttv, googletv, philipstv", 	false),
			ZUNE 				("Zune", 				"zune", 									true),
			IE_MOBILE 			("IEMobile", 			"iemobile", 								true),
			TABLET 				("Tablet", 				"tablet", 									true),
			KINDLE 				("Amazon Kindle", 		"kindle, kf", 								true),
			PLAYBOOK 			("Playbook", 			"playbook", 								true),
			GOOGLE_NEXUS 		("Google Nexus", 		"nexus", 									true),
			MOTOROLA_XOOM 		("Motorola Xoom", 		"xoom", 									true),
			IPOD 				("iPod", 				"ipod", 									true),
			IPAD 				("iPad", 				"ipad", 									true),
			IPHONE 				("iPhone", 				"iphone", 									true),
			BLACKBERRY 			("BlackBerry", 			"blackberry, bb", 							true),
			WINDOWS_PHONE 		("Windows Phone", 		"windows phone", 							true),
			WINDOWS 			("Windows", 			"windows", 									false),
			GENREIC_ANDROID 	("Generic Android", 	"android", 									true);
			
			private String 		wName;
			private String 		wUserAgent;
			private boolean 	wMobile;
			public String 	getName() {return wName;}
			public String 	getUserAgent() {return wUserAgent;}
			public boolean 	isMobile() {return wMobile;}
			
			private DEVICE(String pName, String pUserAgent, boolean pMobile) {
				wName = pName;
				wUserAgent = pUserAgent;
				wMobile = pMobile;
			}
			
			public static DEVICE get(Integer pCode) {
				if (DBSObject.isNull(pCode)) {
					return null;
				}
				switch (pCode) {
				case 0:
					return UNKNOW;
				case 1:
					return MACINTOSH;
				case 2:
					return TV_LG;
				case 3:
					return TV;
				case 4:
					return ZUNE;
				case 5:
					return IE_MOBILE;
				case 6:
					return TABLET;
				case 7:
					return KINDLE;
				case 8:
					return PLAYBOOK;
				case 9:
					return GOOGLE_NEXUS;
				case 10:
					return MOTOROLA_XOOM;
				case 11:
					return IPOD;
				case 12:
					return IPAD;
				case 13:
					return IPHONE;
				case 14:
					return BLACKBERRY;
				case 15:
					return WINDOWS_PHONE;
				case 16:
					return WINDOWS;
				case 17:
					return GENREIC_ANDROID;
				default:
					return UNKNOW;
				}
			}
			
			public static String getDeviceNameFromUserAgent(String pUserAgent) {
				if (DBSObject.isEmpty(pUserAgent)) {return "";}
				return getDeviceFromUserAgent(pUserAgent).wName;
			}
			
			public static DEVICE getDeviceFromUserAgent(String pUserAgent) {
				if (DBSObject.isEmpty(pUserAgent)) {return null;}
				String xUserAgent = pUserAgent.toLowerCase();
				if (DBSString.contains(xUserAgent, DBSString.toArrayList(MACINTOSH.getUserAgent(), ","))) {
					return MACINTOSH;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(TV_LG.getUserAgent(), ","))) {
					return TV_LG;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(TV.getUserAgent(), ","))) {
					return TV;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(ZUNE.getUserAgent(), ","))) {
					return ZUNE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IE_MOBILE.getUserAgent(), ","))) {
					return IE_MOBILE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(TABLET.getUserAgent(), ","))) {
					return TABLET;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(KINDLE.getUserAgent(), ","))) {
					return KINDLE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(PLAYBOOK.getUserAgent(), ","))) {
					return PLAYBOOK;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(GOOGLE_NEXUS.getUserAgent(), ","))) {
					return GOOGLE_NEXUS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(MOTOROLA_XOOM.getUserAgent(), ","))) {
					return MOTOROLA_XOOM;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IPOD.getUserAgent(), ","))) {
					return IPOD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IPAD.getUserAgent(), ","))) {
					return IPAD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IPHONE.getUserAgent(), ","))) {
					return IPHONE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(BLACKBERRY.getUserAgent(), ","))) {
					return BLACKBERRY;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWS_PHONE.getUserAgent(), ","))) {
					return WINDOWS_PHONE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWS.getUserAgent(), ","))) {
					return WINDOWS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(GENREIC_ANDROID.getUserAgent(), ","))) {
					return GENREIC_ANDROID;
				}
				return UNKNOW;
			}
		}
	}
}
