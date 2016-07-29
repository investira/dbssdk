package br.com.dbsoft.util;


import org.junit.Assert;
import org.junit.Test;

public class TstColor {

	@Test
	public void fromRGB(){
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").toRGB().toString(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").toRGBA().toString(), "rgba(39, 117, 165, 1.0)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").toRGB().toString(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").toRGBA().toString(), "rgba(39, 117, 165, 1.0)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").toRGB().toString(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").toRGBA().toString(), "rgba(39, 117, 165, 0.5)");

		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").toHex(), "#2775a5");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").toHex(), "#2775a5");

		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").toHSL().toString(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").toHSLA().toString(), "hsla(202.85715, 61.76471%, 40.0%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").toHSL().toString(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").toHSLA().toString(), "hsla(202.85715, 61.76471%, 40.0%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").toHSL().toString(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").toHSLA().toString(), "hsla(202.85715, 61.76471%, 40.0%, 0.5)");
		
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").toHSB().toString(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("rgb(39, 117, 165)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").toHSB().toString(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 1)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").toHSB().toString(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("rgba(39, 117, 165, 0.5)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 0.5)");
	}

	@Test
	public void fromHSL(){
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").toRGB().toString(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").toRGBA().toString(), "rgba(39, 117, 165, 1.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").toRGB().toString(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").toRGBA().toString(), "rgba(39, 117, 165, 1.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").toRGB().toString(), "rgb(39, 117, 165)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").toRGBA().toString(), "rgba(39, 117, 165, 0.5)");

		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").toHex(), "#2775a5");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").toHex(), "#2775a5");

		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").toHSL().toString(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").toHSLA().toString(), "hsla(202.85715, 61.76471%, 40.0%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").toHSL().toString(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").toHSLA().toString(), "hsla(202.85715, 61.76471%, 40.0%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").toHSL().toString(), "hsl(202.85715, 61.76471%, 40.0%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").toHSLA().toString(), "hsla(202.85715, 61.76471%, 40.0%, 0.5)");

		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").toHSB().toString(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("hsl(202.85715, 61.76471%, 40%)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").toHSB().toString(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").toHSB().toString(), "hsb(202.85715, 76.36363%, 64.70589%)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 0.5)");

		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 1.0)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 1.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.0)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 0.0)");
		Assert.assertEquals(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0)").toHSBA().toString(), "hsba(202.85715, 76.36363%, 64.70589%, 0.0)");
	}

	@Test
	public void toFromColor(){
//		System.out.println(DBSColor.fromString("hsla(202.85715, 61.76471%, 40%, 0.5)").toRGBA().toString());
//		System.out.println(DBSColor.fromString("hsl(120.0, 100.0%, 82.1%)").toHSL().toString());
//		System.out.println(DBSColor.fromString("hsl(120.0,100.0% ,82.0%)").toHSL().toString());
//		System.out.println(DBSColor.fromString("hsl(217.5, 55.17%, 11.37%)").toHSL().toString());
//		System.out.println(DBSColor.fromString("hsla(217.5, 0.2096436%, 0.02384182%, 1.0)").toHSLA().toString());
//		System.out.println(DBSColor.fromString("hsla(217.5, 0.2096436%, 0.02323%, 1)").toHSLA().toString());
		
		
		
	}
	
}