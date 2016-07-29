package br.com.dbsoft.util;

import java.awt.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class DBSColor {
	protected static Logger			wLogger = Logger.getLogger(DBSColor.class);
	
	private interface ALPHA{
		float getAlpha();
	}

	private interface HS{
		float getHue();
		float getSaturation();
	}

	public interface RGB{
		int getRed();
		int getGreen();
		int getBlue();
	}
	
	public interface HSB extends HS{
		float getBrightness();
	}
	public interface HSL extends HS{
		float getLightness();
	}

	public interface RGBA extends RGB, ALPHA{}
	public interface HSBA extends HSB, ALPHA{}
	public interface HSLA extends HSL, ALPHA{}


	private abstract class HSValue implements HS{
		@Override
		public float getHue() {return wHue;}
	}

	
	private class RGBValue implements RGB{
		@Override
		public int getRed() {return wRed;}
		@Override
		public int getGreen() {return wGreen;}
		@Override
		public int getBlue() {return wBlue;}
		
		@Override
		public String toString() {
			return String.format("rgb(%s, %s, %s)", getRed(), getGreen(), getBlue());
		}
	}
	
	private class HSBValue extends HSValue implements HSB{
		@Override
		public float getSaturation() {return wSaturationB;}
		@Override
		public float getBrightness() {return wBrightness;}

		@Override
		public String toString() {
			return String.format("hsb(%s, %s%%, %s%%)", getHue(), getSaturation(), getBrightness());
		}
	}

	private class HSLValue extends HSValue implements HSL{
		@Override
		public float getSaturation() {return wSaturationL;}
		@Override
		public float getLightness() {return wLightness;}

		@Override
		public String toString() {
			return String.format("hsl(%s, %s%%, %s%%)", getHue(), getSaturation(), getLightness());
		}
		
	}

	private class RGBAValue extends RGBValue implements RGBA{
		@Override
		public float getAlpha() {return DBSObject.getNotNull(wAlpha, 1f);}
		@Override
		public String toString() {
			return String.format("rgba(%s, %s, %s, %s)", getRed(), getGreen(), getBlue(), getAlpha());
		}
	}

	private class HSBAValue extends HSBValue implements HSBA{
		@Override
		public float getAlpha() {return wAlpha;}
		@Override
		public String toString() {
			return String.format("hsba(%s, %s%%, %s%%, %s)", getHue(), getSaturation(), getBrightness(), getAlpha());
		}
	}

	public class HSLAValue extends HSLValue implements HSLA{
		@Override
		public float getAlpha() {return wAlpha;}
		@Override
		public String toString() {
			return String.format("hsla(%s, %s%%, %s%%, %s)", getHue(), getSaturation(), getLightness(), getAlpha());
		}
	}

	private RGBValue wRGBValue = new RGBValue();
	private HSBValue wHSBValue = new HSBValue();
	private HSLValue wHSLValue = new HSLValue();
	private RGBAValue wRGBAValue = new RGBAValue();
	private HSBAValue wHSBAValue = new HSBAValue();
	private HSLAValue wHSLAValue = new HSLAValue();
	
	private int wRed;
	private int wGreen;
	private int wBlue;
	private float wHue;
	private float wSaturationB;
	private float wBrightness;
	private float wSaturationL;
	private float wLightness;
	private float wAlpha = 1f;

	public DBSColor(int pRed, int pGreen, int pBlue, float pAlpha) {
		pvSetColorAttributes(this, pRed, pGreen, pBlue, pAlpha);
	}

	public DBSColor(String pColorString){
		DBSColor xColor = fromString(pColorString);
		pvSetColorAttributes(this, xColor.wRed, xColor.wGreen, xColor.wBlue, xColor.wAlpha);
	}

	/**
	 * Cria cor a partir de uma string: ex:rgb(10,23,42), hsl(200, 20%, 10%)
	 * @param pColor
	 * @return
	 */
	public static DBSColor fromString(String pColorString) {
		pColorString = pColorString.trim().toLowerCase();
		for (Converter xConverter : CONVERTERS) {
			DBSColor xColor = xConverter.getColor(pColorString);
			if (xColor != null) {
				return xColor;
			}
		}
		wLogger.error(String.format("Não foi possível converter %s em DBSColor.", pColorString));
		return null;
	}
	
	
	public String toHex() {
		return String.format("#%02x%02x%02x", wRGBValue.getRed(), wRGBValue.getGreen(), wRGBValue.getBlue());
	}
	public RGB toRGB(){
		return wRGBValue;
	}
	public RGBA toRGBA(){
		return wRGBAValue;
	}
	public HSB toHSB(){
		return wHSBValue;
	}
	public HSBA toHSBA(){
		return wHSBAValue;
	}
	public HSL toHSL(){
		return wHSLValue;
	}
	public HSLA toHSLA(){
		return wHSLAValue;
	}

	@Override
	public boolean equals(Object pColor) {
		if (pColor == null) {
			return false;
		}

		if (!(pColor instanceof DBSColor)) {
			return false;
		}

		return toRGBA().equals(((DBSColor) pColor).toRGBA());
	}

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

	
	/**
	 * Configura atributos do DBSColor informado utilizando os RGBA.
	 * @param pDBSColor
	 * @param pRed
	 * @param pGreen
	 * @param pBlue
	 * @param pAlpha
	 * @return
	 */
	private static DBSColor pvSetColorAttributes(DBSColor pDBSColor, int pRed, int pGreen, int pBlue, float pAlpha) {
		//ALPHA
		pDBSColor.wAlpha = pAlpha;
		//RGB
		pDBSColor.wRed = pRed;
		pDBSColor.wGreen = pGreen;
		pDBSColor.wBlue = pBlue;
		float[] xHSValues = new float[3];
		//HSL
		xHSValues = pvGetHSL(pRed, pGreen, pBlue); 
		pDBSColor.wHue = xHSValues[0];
		pDBSColor.wSaturationL = xHSValues[1];
		pDBSColor.wLightness = xHSValues[2];
		//HSB
		xHSValues = pvGetHSB(pRed, pGreen, pBlue); 
		pDBSColor.wSaturationB = xHSValues[1];
		pDBSColor.wBrightness = xHSValues[2];
		return pDBSColor;
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
				float xAlpha = 1f;
				if (xMatcher.groupCount() >= 4) {
					xAlpha = Float.parseFloat(xMatcher.group(4));
				}
				return createColor(xMatcher, xAlpha);
			}
			return null;
		}

		protected DBSColor createColor(Matcher pMatcher, float pAlpha) {
			return new DBSColor(fromMatchGroup(pMatcher, 1), fromMatchGroup(pMatcher, 2), fromMatchGroup(pMatcher, 3), pAlpha);
		}

		protected short fromMatchGroup(Matcher pMatcher, int pIndex) {
			return Short.parseShort(pMatcher.group(pIndex), 10);
		}

		protected abstract Pattern getPattern();
	}

	private static class RgbConverter extends Converter {
		private static final Pattern RGB_PATTERN = Pattern.compile("^\\s*rgb\\(" + 
																	"\\s*(\\d{1,3})\\s*," + 
																	"\\s*(\\d{1,3})\\s*," + 
																	"\\s*(\\d{1,3})\\s*\\)\\s*$");

		@Override
		protected Pattern getPattern() {
			return RGB_PATTERN;
		}
	}

	private static class RgbPctConverter extends Converter {
		private static final Pattern RGBPCT_PATTERN = Pattern.compile("^\\s*rgb\\(" + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*\\)\\s*$");

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
		private static final Pattern RGBA_PATTERN = Pattern.compile("^\\s*rgba\\(" + 
																	"\\s*(\\d{1,3})\\s*," +
																	"\\s*(\\d{1,3})\\s*," + 
																	"\\s*(\\d{1,3})\\s*," + 
																	"\\s*((1|0\\.\\d+)|(1.0)|(0))\\s*\\)\\s*$");

		@Override
		protected Pattern getPattern() {
			return RGBA_PATTERN;
		}
	}

	private static class RgbaPctConverter extends RgbPctConverter {
		private static final Pattern RGBAPCT_PATTERN = Pattern.compile("^\\s*rgba\\(" + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," + 
																	"\\s*((1|0\\.\\d+)|(1.0)|(0))\\s*\\)\\s*$");

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
		private static final Pattern HSL_PATTERN = Pattern.compile("^\\s*hsl\\(" + 
																"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)\\s*," + 
																"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," + 
																"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*\\)\\s*$");
		
		@Override
		protected Pattern getPattern() {
			return HSL_PATTERN;
		}

		@Override
		protected DBSColor createColor(Matcher pMatcher, float pAlpha) {
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

			return new DBSColor((short) Math.round(xRed * 255), (short) Math.round(xGreen * 255), (short) Math.round(xBlue * 255), pAlpha);
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
		private static final Pattern HSLA_PATTERN = Pattern.compile("^\\s*hsla\\(" + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)\\s*," + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," + 
																	"\\s*(\\d{1,3}|\\d{1,3}\\.\\d+)%\\s*," +
																	"\\s*((1|0\\.\\d+)|(1.0)|(0))\\s*\\)\\s*$");
		@Override
		protected Pattern getPattern() {
			return HSLA_PATTERN;
		}

	}

}

	
	

