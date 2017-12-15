package test;

import static org.junit.Assert.*;

import org.junit.Test;


import mainconcat.CreateNumConcat;

public class CreateNumConcatText {
	CreateNumConcat cnc;
	@Test
	public void testRemovingTitles() {
		String jsonCun = "{\"firstName\": \"Jonh\",\"dateBirth\":18/10/80 ,\"nationality\" : “ES” ,\"surname\": \"Maria\"}";
		cnc = new CreateNumConcat(jsonCun);
		assertEquals("Jonh",     cnc.removingTitles("ms Jonh"));
		assertEquals("Ludwig",   cnc.removingTitles("atty Ludwig"));
		assertEquals("Victor",   cnc.removingTitles("honorable Victor"));
		assertEquals("Lan",      cnc.removingTitles("ph.d Lan"));
		assertEquals("Amy-Ally", cnc.removingTitles("miss Amy-Ally"));
		assertEquals("Giovani",  cnc.removingTitles("monsieur Giovani"));
		assertEquals("Güter",    cnc.removingTitles("pres Güter"));
		assertEquals("lu oscar", cnc.removingTitles("lu oscar"));
	}
	
	@Test
	public void testRemovingPrefixes() {
		String jsonCun = "{\"firstName\": \"Jonh\",\"dateBirth\":18/10/80 ,\"nationality\" : “ES” ,\"surname\": \"Maria\"}";
		cnc = new CreateNumConcat(jsonCun);
		assertEquals("Jonh",     cnc.removingPrefixes("del Jonh"));
		assertEquals("Ludwig",   cnc.removingPrefixes("Ludwig"));
		assertEquals("Victor",   cnc.removingPrefixes("ui Victor"));
		assertEquals("Lan",      cnc.removingPrefixes("mac Lan"));
		assertEquals("Amy-Ally", cnc.removingPrefixes("von dem Amy-Ally"));
		assertEquals("" ,  cnc.removingPrefixes(""));
		assertEquals("Güter Rober",    cnc.removingPrefixes("do Güter Rober"));
	}
	
	@Test
	public void transliteration() {
		String jsonCun = "{'firstName':'Jonh','surname':'Maria','dateBirth':25-12-2005,'nationality':'ES'}";
		cnc = new CreateNumConcat(jsonCun);
		assertEquals("",     cnc.transliteration(""));
		assertEquals("A####",   cnc.transliteration("a"));
		assertEquals("AA###",   cnc.transliteration("a2a"));
		assertEquals("AAAA#",      cnc.transliteration("ăâăâ"));
		assertEquals("RRRRR", cnc.transliteration("rrrrrrrr"));
		assertEquals("FSSSS" ,  cnc.transliteration("f-ssss"));
		assertEquals("GUTER",    cnc.transliteration("Güter Rober"));
	}
	
	@Test
	public void generateConcatText() {
		String jsonCun = "{'firstName':'Jonh','surname':'Maria','dateBirth':25-12-2005,'nationality':'ES'}";
		cnc = new CreateNumConcat(jsonCun);
		assertEquals("19800113JONH#OBRIA",   cnc.generateConcat("{'firstName':'Jonh','surname':'O´Brian','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals("19800113LUDWIROHE#",   cnc.generateConcat("{'firstName':'Ludwig','surname':'Van der Rohe','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals("19800113VICTONDEGA",   cnc.generateConcat("{'firstName':'Victor','surname':'Ñdegă','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals("19800113ELI##BRUIJ",   cnc.generateConcat("{'firstName':'Eli','surname':'de Bruijn','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals("19800113WILEKDEVIT",   cnc.generateConcat("{'firstName':'Wileke','surname':'Devitt','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals("19800113AMYALGARCA" ,  cnc.generateConcat("{'firstName':'Amy-Ally','surname':'Garçăo de Mgallanes','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals("19800113GIOVASANTO",   cnc.generateConcat("{'firstName':'Giovani','surname':'dos Santos','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals("19800113GIOVASANTO",   cnc.generateConcat("{'firstName':'Giovani','surname':'dos Santos','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals(null,   cnc.generateConcat("{'firstName':'','surname':'dos Santos','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals(null,   cnc.generateConcat("{'firstName':'Giovani','surname':'','dateBirth':1980-01-13,'nationality':'ES'}"));
		assertEquals(null,   cnc.generateConcat("{'firstName':'Giovani','surname':'dos Santos','dateBirth':,'nationality':'ES'}"));
		assertEquals(null,   cnc.generateConcat("{'firstName':'','surname':'','dateBirth':,'nationality':''}"));
	}
	
	@Test
	public void text() throws InterruptedException {
		CreateNumConcat con = new CreateNumConcat("{'firstName':'Jonh','surname':'O´Brian','dateBirth':1980-01-13,'nationality':'ES'}");
		con.start();
		CreateNumConcat con1 = new CreateNumConcat("{'firstName':'Ludwig','surname':'Van der Rohe','dateBirth':1980-01-13,'nationality':'ES'}");
		con1.start();
		CreateNumConcat con2 = new CreateNumConcat("{'firstName':'Victor','surname':'Ñdegă','dateBirth':1980-01-13,'nationality':'ES'}");
		con2.start();
		CreateNumConcat con3 = new CreateNumConcat("{'firstName':'Eli','surname':'de Bruijn','dateBirth':1980-01-13,'nationality':'ES'}");
		con3.start();
		CreateNumConcat con4 = new CreateNumConcat("{'firstName':'Eli','surname':'de Bruijn','dateBirth':1980-01-13,'nationality':'ES'}");
		con4.start();
		CreateNumConcat con5 = new CreateNumConcat("{'firstName':'Wileke','surname':'Devitt','dateBirth':1980-01-13,'nationality':'ES'}");
		con5.start();
		CreateNumConcat con6 = new CreateNumConcat("{'firstName':'Amy-Ally','surname':'Garçăo de Mgallanes','dateBirth':1980-01-13,'nationality':'ES'}");
		con6.start();
		CreateNumConcat con7 = new CreateNumConcat("{'firstName':'','surname':'dos Santos','dateBirth':1980-01-13,'nationality':'ES'}");
		con7.start();
		CreateNumConcat con8 = new CreateNumConcat("{'firstName':'Giovani','surname':'','dateBirth':1980-01-13,'nationality':'ES'}");
		con8.start();
		CreateNumConcat con9 = new CreateNumConcat("{'firstName':'','surname':'dos Santos','dateBirth':,'nationality':'ES'}");
		con9.start();
		CreateNumConcat con10 = new CreateNumConcat("{'firstName':'','surname':'','dateBirth':,'nationality':'ES'}");
		con10.start();
	}
}
