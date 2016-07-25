package br.com.dbsoft.util;

import java.awt.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class DBSColor {
	protected static Logger			wLogger = Logger.getLogger(DBSColor.class);
	
	private int wRed;
	private int wGreen;
	private int wBlue;
	private float wHue;
	private float wSaturation;
	private float wLightness;
	private double wAlpha;

	private static final Converter[] CONVERTERS = { 
			new RgbConverter(), 
			new RgbPctConverter(), 
			new RgbaConverter(),
			new RgbaPctConverter(), 
			new HexConverter(), 
			new Hex3Converter(), 
			new HslConverter(), 
			new HslaConverter() 
	};

	public float getHue() {return wHue;}
	public void setHue(float pHue) {wHue = pHue;}

	public float getSaturation() {return wSaturation;}
	public void setSaturation(float pSaturation) {wSaturation = pSaturation;}

	public float getLightness() {return wLightness;}
	public void setLightness(float pLightness) {wLightness = pLightness;}

	public int getRed() {return wRed;}
	public void setRed(int pRed) {wRed = pRed;}

	public int getGreen() {return wGreen;}
	public void setGreen(int pGreen) {wGreen = pGreen;}

	public int getBlue() {return wBlue;}
	public void setBlue(int pBlue) {wBlue = pBlue;}

	public double getAlpha() {return wAlpha;}
	public void setAlpha(double pAlpha) {wAlpha = pAlpha;}

	/*
	 * Guesses what format the input color is in.
	 */
	public static DBSColor fromString(String pColor) {
		pColor = pColor.trim().toLowerCase();
		for (Converter converter : CONVERTERS) {
			DBSColor color = converter.getColor(pColor);
			if (color != null) {
				return color;
			}
		}
		wLogger.error(String.format("Não foi possível converter %s em DBSColor.", pColor));
		return null;
	}
	

	
	public String asRgb() {
		return String.format("rgb(%d, %d, %d)", wRed, wGreen, wBlue);
	}

	public String asRgba() {
		String alphaString;
		if (wAlpha == 1) {
			alphaString = "1";
		} else if (wAlpha == 0) {
			alphaString = "0";
		} else {
			alphaString = Double.toString(wAlpha);
		}
		return String.format("rgba(%d, %d, %d, %s)", wRed, wGreen, wBlue, alphaString);
	}

	public String asHex() {
		return String.format("#%02x%02x%02x", wRed, wGreen, wBlue);
	}

	public String asHsba(){
		String alphaString;
		if (wAlpha == 1) {
			alphaString = "1";
		} else if (wAlpha == 0) {
			alphaString = "0";
		} else {
			alphaString = Double.toString(wAlpha);
		}
		float[] xHSBValues = pvGetHSB(getRed(), getGreen(), getBlue());
		return String.format("hsba(%s, %s, %s, %s)", xHSBValues[0], xHSBValues[1] + "%", xHSBValues[2] + "%", alphaString);
	}
	
	public String asHsb(){
		float[] xHSBValues = pvGetHSB(getRed(), getGreen(), getBlue());
		return String.format("hsb(%s, %s, %s)", xHSBValues[0], xHSBValues[1] + "%", xHSBValues[2] + "%");
	}
	
	public String asHsla(){
		String alphaString;
		if (wAlpha == 1) {
			alphaString = "1";
		} else if (wAlpha == 0) {
			alphaString = "0";
		} else {
			alphaString = Double.toString(wAlpha);
		}
		float[] xHSLValues = pvGetHSL(getRed(), getGreen(), getBlue());
		return String.format("hsla(%s, %s, %s, %s)", xHSLValues[0], xHSLValues[1] + "%", xHSLValues[2] + "%", alphaString);
	}
	
	public String asHsl(){
		float[] xHSLValues = pvGetHSL(getRed(), getGreen(), getBlue());
		return String.format("hsl(%s, %s, %s)", xHSLValues[0], xHSLValues[1] + "%", xHSLValues[2] + "%");
	}

	@Override
	public String toString() {
		return "Color: " + asRgba();
	}

	@Override
	public boolean equals(Object pColor) {
		if (pColor == null) {
			return false;
		}

		if (!(pColor instanceof DBSColor)) {
			return false;
		}

		return asRgba().equals(((DBSColor) pColor).asRgba());
	}

	private static DBSColor pvFromRGBA(int pRed, int pGreen, int pBlue, double pAlpha) {
		DBSColor xColor = new DBSColor();
		float[] xHSBValues = new float[3];
		xColor.setRed(pRed);
		xColor.setGreen(pGreen);
		xColor.setBlue(pBlue);
		xColor.setAlpha(pAlpha);
		xHSBValues = pvGetHSL(pRed, pGreen, pBlue); 
		xColor.setHue(xHSBValues[0]);
		xColor.setSaturation(xHSBValues[1]);
		xColor.setLightness(xHSBValues[2]);
		return xColor;
	}
	
	private static float[] pvGetHSB(int pRed, int pGreen, int pBlue){
		float[] xHSBValues = new float[3];
		xHSBValues = Color.RGBtoHSB(pRed, pGreen, pBlue, xHSBValues);
		xHSBValues[0] = DBSNumber.multiply(xHSBValues[0], 360).floatValue();
		xHSBValues[1] = DBSNumber.multiply(xHSBValues[1], 100).floatValue();
		xHSBValues[2] = DBSNumber.multiply(xHSBValues[2], 100).floatValue();
		return xHSBValues;
	}
	
	
	private static float[] pvGetHSL(int pRed, int pGreen, int pBlue) {
		float[] xHSLValues = new float[3];
		float xRed = DBSNumber.divide(pRed, 255).floatValue();
		float xGreen = DBSNumber.divide(pGreen, 255).floatValue();
		float xBlue = DBSNumber.divide(pBlue, 255).floatValue();
		float xMax = Math.max(Math.max(xRed, xGreen), xBlue);
		float xMin = Math.min(Math.min(xRed, xGreen), xBlue);
		float xS;
		float xL = (xMax + xMin) / 2f;
		float xH = 0f;
		if (xMax == xMin){
			xS = 0f;
		}else{
			float xD = xMax - xMin;
			if (xL > .5f){
				xS = xD / (2f - xMax - xMin);
			}else{
				xS = xD / (xMax + xMin);
			}
			if (xMax == xRed){
				xH = (xGreen - xBlue) / xD + (xGreen < xBlue ? 6f : 0f);
			}else if (xMax == xGreen){
				xH = (xBlue - xRed) / xD + 2f;
			}else if (xMax == xBlue){
				xH = (xRed - xGreen) / xD + 4f;
			}
			xH /= 6;
		}
		xHSLValues[0] = 360 * xH;
		xHSLValues[1] = xS * 100;
		xHSLValues[2] = xL * 100;
		return xHSLValues;
	}

	

	private static abstract class Converter {
		public DBSColor getColor(String pString) {
			Matcher xMatcher = getPattern().matcher(pString);
			if (xMatcher.find()) {
				double xAlpha = 1.0;
				if (xMatcher.groupCount() == 4) {
					xAlpha = Double.parseDouble(xMatcher.group(4));
				}
				return createColor(xMatcher, xAlpha);
			}
			return null;
		}

		protected DBSColor createColor(Matcher pMatcher, double pAlpha) {
			return DBSColor.pvFromRGBA(fromMatchGroup(pMatcher, 1), fromMatchGroup(pMatcher, 2), fromMatchGroup(pMatcher, 3), pAlpha);
		}

		protected short fromMatchGroup(Matcher pMatcher, int pIndex) {
			return Short.parseShort(pMatcher.group(pIndex), 10);
		}

		protected abstract Pattern getPattern();
	}

	private static class RgbConverter extends Converter {
		private static final Pattern RGB_PATTERN = Pattern.compile("^\\s*rgb\\(\\s*" + 
																	"(\\d{1,3})\\s*,\\s*" + 
																	"(\\d{1,3})\\s*,\\s*" + 
																	"(\\d{1,3})\\s*\\)\\s*$");

		@Override
		protected Pattern getPattern() {
			return RGB_PATTERN;
		}
	}

	private static class RgbPctConverter extends Converter {
		private static final Pattern RGBPCT_PATTERN = Pattern.compile("^\\s*rgb\\(\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*\\)\\s*$");

		@Override
		protected Pattern getPattern() {
			return RGBPCT_PATTERN;
		}

		@Override
		protected short fromMatchGroup(Matcher pMatcher, int pIndex) {
			double n = Double.parseDouble(pMatcher.group(pIndex)) / 100 * 255;
			return (short) n;
		}
	}

	private static class RgbaConverter extends RgbConverter {
		private static final Pattern RGBA_PATTERN = Pattern.compile("^\\s*rgba\\(\\s*" + 
																	"(\\d{1,3})\\s*,\\s*" +
																	"(\\d{1,3})\\s*,\\s*" + 
																	"(\\d{1,3})\\s*,\\s*" + 
																	"(0|1|0\\.\\d+)\\s*\\)\\s*$");

		@Override
		protected Pattern getPattern() {
			return RGBA_PATTERN;
		}
	}

	private static class RgbaPctConverter extends RgbPctConverter {
		private static final Pattern RGBAPCT_PATTERN = Pattern.compile("^\\s*rgba\\(\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" + 
																	"(0|1|0\\.\\d+)\\s*\\)\\s*$");

		@Override
		protected Pattern getPattern() {
			return RGBAPCT_PATTERN;
		}
	}

	private static class HexConverter extends Converter {
		private static final Pattern HEX_PATTERN = Pattern.compile("#(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})");

		@Override
		protected Pattern getPattern() {
			return HEX_PATTERN;
		}

		@Override
		protected short fromMatchGroup(Matcher pMatcher, int pIndex) {
			return Short.parseShort(pMatcher.group(pIndex), 16);
		}
	}

	private static class Hex3Converter extends Converter {
		private static final Pattern HEX3_PATTERN = Pattern.compile("#(\\p{XDigit}{1})(\\p{XDigit}{1})(\\p{XDigit}{1})");

		@Override
		protected Pattern getPattern() {
			return HEX3_PATTERN;
		}

		@Override
		protected short fromMatchGroup(Matcher pMatcher, int pIndex) {
			return Short.parseShort(pMatcher.group(pIndex) + pMatcher.group(pIndex), 16);
		}

	}

	private static class HslConverter extends Converter {
		private static final Pattern HSL_PATTERN = Pattern.compile("^\\s*hsl\\(\\s*" + 
																"(\\d{1,3}|\\d{1,3}\\.\\d+)\\s*,\\s*" + 
																"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" + 
																"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*\\)\\s*$");
		
		@Override
		protected Pattern getPattern() {
			return HSL_PATTERN;
		}

		@Override
		protected DBSColor createColor(Matcher pMatcher, double a) {
			double xHue = Double.parseDouble(pMatcher.group(1)) / 360;
			double xSaturation = Double.parseDouble(pMatcher.group(2)) / 100;
			double xLightness = Double.parseDouble(pMatcher.group(3)) / 100;
			double xRed, xGreen, xBlue;

			if (xSaturation == 0) {
				xRed = xLightness;
				xGreen = xRed;
				xBlue = xRed;
			} else {
				double xLuminocity2 = (xLightness < 0.5) ? xLightness * (1 + xSaturation) : xLightness + xSaturation - xLightness * xSaturation;
				double xLuminocity1 = 2 * xLightness - xLuminocity2;
				xRed = pvHueToRgb(xLuminocity1, xLuminocity2, xHue + 1.0 / 3.0);
				xGreen = pvHueToRgb(xLuminocity1, xLuminocity2, xHue);
				xBlue = pvHueToRgb(xLuminocity1, xLuminocity2, xHue - 1.0 / 3.0);
			}

			return DBSColor.pvFromRGBA((short) Math.round(xRed * 255), (short) Math.round(xGreen * 255), (short) Math.round(xBlue * 255), a);
		}

		private double pvHueToRgb(double pLuminocity1, double pLuminocity2, double pHue) {
			if (pHue < 0.0)
				pHue += 1;
			if (pHue > 1.0)
				pHue -= 1;
			if (pHue < 1.0 / 6.0)
				return (pLuminocity1 + (pLuminocity2 - pLuminocity1) * 6.0 * pHue);
			if (pHue < 1.0 / 2.0)
				return pLuminocity2;
			if (pHue < 2.0 / 3.0)
				return (pLuminocity1 + (pLuminocity2 - pLuminocity1) * ((2.0 / 3.0) - pHue) * 6.0);
			return pLuminocity1;
		}

	}

	private static class HslaConverter extends HslConverter {
		private static final Pattern HSLA_PATTERN = Pattern.compile("^\\s*hsla\\(\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)\\s*,\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" + 
																	"(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*,\\s*" +
																	"(0|1|0\\.\\d+)\\s*\\)\\s*$");

		@Override
		protected Pattern getPattern() {
			return HSLA_PATTERN;
		}

	}

}

	
	

