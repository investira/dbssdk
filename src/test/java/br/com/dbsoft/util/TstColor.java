package br.com.dbsoft.util;


import org.junit.Assert;
import org.junit.Test;

public class TstColor {

	@Test
	public void fromRGB(){
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").asRgb(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").asRgba(), "rgba(39, 117, 165, 1)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").asRgb(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").asRgba(), "rgba(39, 117, 165, 1)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").asRgb(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").asRgba(), "rgba(39, 117, 165, 0.5)");

		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").asHex(), "#2775a5");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").asHex(), "#2775a5");

		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").asHsl(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").asHsla(), "hsla(202.85715, 61.76471%, 40.0%, 1)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").asHsl(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").asHsla(), "hsla(202.85715, 61.76471%, 40.0%, 1)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").asHsl(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").asHsla(), "hsla(202.85715, 61.76471%, 40.0%, 0.5)");
		
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").asHsb(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 1)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").asHsb(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 1)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").asHsb(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 0.5)");
	}

	@Test
	public void fromHSL(){
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").asRgb(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").asRgba(), "rgba(39, 117, 165, 1)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").asRgb(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").asRgba(), "rgba(39, 117, 165, 1)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").asRgb(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").asRgba(), "rgba(39, 117, 165, 0.5)");

		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").asHex(), "#2775a5");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").asHex(), "#2775a5");

		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").asHsl(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").asHsla(), "hsla(202.85715, 61.76471%, 40.0%, 1)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").asHsl(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").asHsla(), "hsla(202.85715, 61.76471%, 40.0%, 1)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").asHsl(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").asHsla(), "hsla(202.85715, 61.76471%, 40.0%, 0.5)");

		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").asHsb(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 1)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").asHsb(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 1)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").asHsb(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 0.5)");

		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1.0)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 1)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.0)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0)").asHsba(), "hsba(202.85715, 76.36363%, 64.70589%, 0)");
	}

	@Test
	public void toFromColor(){
//		System.out.println(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").asRgba());
//		System.out.println(DBSColor.fromString("hsl(120.0, 100.0%, 82.1%)").asHsl());
//		System.out.println(DBSColor.fromString("hsl(120.0,100.0% ,82.0%)").asHsl());
//		System.out.println(DBSColor.fromString("hsl(217.5, 55.17%, 11.37%)").asHsl());
		System.out.println(DBSColor.fromString("hsla(217.5, 0.2096436%, 0.02384182%, 1.0)").asHsla());
//		System.out.println(DBSColor.fromString("hsla(217.5, 0.2096436%, 0.02323%, 1)").asHsla());
		
		
		
	}
	
}