package mainconcat;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import entity.Customer;

/**
 * the routine refers to CONCAT, the person will be identified using the
 * concatenation of following elements in the following order: (i) the person's
 * date of birth; (ii) the first five characters of the first name; (iii) the
 * first five characters of the surname. The priority of the codes must be
 * strictly followed for each jurisdiction A combination of the name and date of
 * birth, that is, CONCAT code. https://equiniti.com/media/3381/2-data-requirements-summary.pdf
 * 
 * @author oscar Lozano
 * @version 1.0
 */
public class CreateNumConcat extends Thread {

	private String jCustomer;
	private String codeConcat;
	private static Map<String, Character> map;

	final String[] TITLES = { "ATTY", "COACH", "DAME", "DR", "FR", "GOV", "HONORABLE", "MADAM", "MADAME", "MAID",
			"MASTER", "MISS", "MONSIEUR", "MR", "MRS", "MS", "MX", "OFC", "PH.D", "PRES", "PROF", "REV", "SIR" };

	final String[] PREFIXES = { "AM", "AUF", "AUF DEM", "AUS DER", "D", "DA", "DE", "DE L’", "DEL", "DE LA", "DE LE",
			"DI", "DO", "DOS", "DU", "IM", "LA", "LE", "MAC", "MC", "MHAC", "MHÍC", "MHIC GIOLLA", "MIC", "NI", "NÍ",
			"NÍC", "O", "Ó", "UA", "UI", "UÍ", "VAN", "VAN DE", "VAN DEN", "VAN DER", "VOM", "VON", "VON DEM", "VON",
			"DEN", "VON DER" };

	public CreateNumConcat(String jCustomer) {
		this.jCustomer = jCustomer;
		map = new HashMap<String, Character>();
		mapUnicode();
	}

	public String getCodeConcat() {
		return codeConcat;
	}
	public void setCodeConcat(String codConcat) {
		this.codeConcat = codConcat;
	}

	public void run() {
		this.setCodeConcat(this.generateConcat(jCustomer));
	}

	/**
	 * Method that checks all the data to be able to concatenate the code correctly.
	 *  @param json file belonging to a client.
	 * @return returns the already formed CONCAT code and null if any of the
	 *         required fields is not correct or is blank.
	 */
	public synchronized String generateConcat(String jCustomer) {
		try {
			Gson gson = new Gson();
			Customer cust = gson.fromJson(jCustomer, Customer.class);

			cust.setFirstName(transliteration(removingPrefixes(removingTitles(cust.getFirstName()))));
			cust.setSurname(transliteration(removingPrefixes(removingTitles(cust.getSurname()))));
			cust.setDateBirth(cust.getDateBirth());
			cust.setNationality(cust.getNationality());
			if ((cust.getFirstName().length() == 0) || (cust.getSurname().length() == 0)
					|| (cust.getDateBirth() == null) || (cust.getNationality().equals("")))
				return this.codeConcat = null;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateBirth = sdf.format(cust.getDateBirth());
			return this.codeConcat = dateBirth + cust.getFirstName() + cust.getSurname(); // cust.getNationality();
		} catch (JsonSyntaxException e) {
			return this.codeConcat = null;
		}
	}

	/**
	 * method removing titles, the name that donate titles, positon, profession or
	 * academic qualificatons, are to be removed
	 * @param string of characters, such as the name or surname.
	 * @return returns the name without the titles that appear in the collection
	 */
	public String removingTitles(String name) {
		String solution = null;
		String resul = null;
		StringTokenizer tokens = new StringTokenizer(name);
		while (tokens.hasMoreTokens()) {
			resul = tokens.nextToken();
			for (String i : TITLES) {
				if (resul.toUpperCase().equalsIgnoreCase(i)) {
					solution = name.substring(i.length() + 1, name.length());
				}
			}
		}
		return solution == null ? name : solution;
	}

	/**
	 * method removing prefixes to surname that are not included above, or prefixes
	 * attached to the name.
	 * 
	 * @param string of characters, such as the name or surname.
	 * @return returns the name without the prefixes that appear in the collection
	 */
	public String removingPrefixes(String namex) {
		String solution = null;
		String name = namex.replace("'","' ");
		for (int i=name.length(); i>0; i--) {
			if (i>0 && name.substring(i-1, i).equals(" ")) {
				for(int j = 0; j<PREFIXES.length; j++) {
					if(name.substring(0, i-1).toUpperCase().equals(PREFIXES[j])) { 
						solution = name.substring(i, name.length());
    	   					i = 1;
    	   				}
				}    
	        }
        }
		return solution == null ? name : solution;
	}

	/**
	 * method Transliteration of apostrophes, accents, hyphens, spaces and similiar.
	 * Generally described the transliteration leavea any English A-Z or a-z
	 * character untouched and removes all the diacitics, apostrophes, hyphens,
	 * puctiation marcks and spaces. This table should be aplied to first name and
	 * and surname prior to obtaining the five first characters. for any names that
	 * are write in Cyrillc, Greek or any other non-Latin laphabet, they should be
	 * deleted and replaced by the characters set aside.
	 * 
	 * @param string
	 *            of characters, such as the name or surname.
	 * @return returns the value of five positions to concatenate with the CONCAT
	 *         code
	 */
	public String transliteration(String name) {
		Character character = null;
		StringBuffer sb = new StringBuffer();
		for (int x = 0; x < name.length(); x++) {
			if (name.toUpperCase().charAt(x) <= 'Z' && name.toUpperCase().charAt(x) >= 'A') {
				sb.append(name.charAt(x));
			} else {
				character = map.get((String.format("\\U+%04x", (int) name.charAt(x))).subSequence(1,
						(String.format("\\U+%04x", (int) name.charAt(x))).length()));
				if (character != null)
					sb.append(character);
			}
		}
		if (sb.length() == 0) 
			return "";
		
		
		return sb.toString().length() < 5 ? (sb.toString() + "#####").toUpperCase().substring(0, 5)
				: sb.toString().toUpperCase().substring(0, 5);
	}
	
	private void mapUnicode() {
		map.put("U+00c4", 'A');map.put("U+00e4", 'A');map.put("U+00c0", 'A');map.put("U+00e0", 'A');map.put("U+00c1", 'A');map.put("U+00e1", 'A');
		map.put("U+00c2", 'A');map.put("U+00e2", 'A');map.put("U+00c3", 'A');map.put("U+00e3", 'A');map.put("U+00c5", 'A');map.put("U+00e5", 'A');
		map.put("U+01cd", 'A');map.put("U+01cE", 'A');map.put("U+0104", 'A');map.put("U+0105", 'A');map.put("U+0102", 'A');map.put("U+0103", 'A');
		map.put("U+00c6", 'A');map.put("U+00e6", 'A');
		
		map.put("U+00c7", 'C');map.put("U+00e7", 'C');map.put("U+0106", 'C');map.put("U+0107", 'C');map.put("U+0108", 'C');map.put("U+0109", 'C');
		map.put("U+010c", 'C');map.put("U+010d", 'C');
		
		map.put("U+010E", 'D');map.put("U+0111", 'D');map.put("U+0110", 'D');map.put("U+010F", 'D');map.put("U+00F0", 'D');
		
		map.put("U+00c8", 'E');map.put("U+00e8", 'E');map.put("U+00c9", 'E');map.put("U+00e9", 'E');map.put("U+00ca", 'E');map.put("U+00ea", 'E');
		map.put("U+00cB", 'E');map.put("U+00eB", 'E');map.put("U+011a", 'E');map.put("U+011b", 'E');map.put("U+0118", 'E');map.put("U+0119", 'E');
		
		map.put("U+011c", 'G');map.put("U+011d", 'G');map.put("U+0122", 'G');map.put("U+0123", 'G');map.put("U+011e", 'G');map.put("U+011f", 'G');
		
		map.put("U+0124", 'H');map.put("U+0125", 'H');
		
		map.put("U+00cc", 'I');map.put("U+00ec", 'I');map.put("U+00cd", 'I');map.put("U+00ed", 'I');map.put("U+00ce", 'I');map.put("U+00ee", 'I');
		map.put("U+00cf", 'I');map.put("U+00ef", 'I');map.put("U+0131", 'I');
		
		map.put("U+0134", 'J');map.put("U+0135", 'J');
		
		map.put("U+0136", 'K');map.put("U+0137", 'K');
		
		map.put("U+0139", 'L');map.put("U+013a", 'L');map.put("U+013b", 'L');map.put("U+013c", 'L');map.put("U+0141", 'L');map.put("U+0142", 'L');
		map.put("U+013d", 'L');map.put("U+013e", 'L');
		
		map.put("U+00d1", 'N');map.put("U+00d1", 'N');map.put("U+0143", 'N');map.put("U+0144", 'N');map.put("U+0147", 'N');map.put("U+0148", 'N');
		
		map.put("U+00d6", 'O');map.put("U+00f6", 'O');map.put("U+00d2", 'O');map.put("U+00f2", 'O');map.put("U+00d3", 'O');map.put("U+00f3", 'O');
		map.put("U+00d4", 'O');map.put("U+00f4", 'O');map.put("U+00d5", 'O');map.put("U+00f5", 'O');map.put("U+0105", 'O');map.put("U+0151", 'O');
		map.put("U+00d8", 'O');map.put("U+00f8", 'O');map.put("U+0152", 'O');map.put("U+0153", 'O');
		
		map.put("U+0154", 'R');map.put("U+0155", 'R');map.put("U+0158", 'R');map.put("U+0159", 'R');
		
		map.put("U+1e9E", 'S');map.put("U+00df", 'S');map.put("U+015a", 'S');map.put("U+015b", 'S');map.put("U+015c", 'S');map.put("U+015d", 'S');
		map.put("U+015e", 'S');map.put("U+015f", 'S');map.put("U+0160", 'S');map.put("U+0161", 'S');map.put("U+0218", 'S');map.put("U+0219", 'S');
		
		map.put("U+0164", 'T');map.put("U+0165", 'T');map.put("U+0162", 'T');map.put("U+0163", 'T');map.put("U+00de", 'T');map.put("U+00fe", 'T');
		map.put("U+021a", 'T');map.put("U+021B", 'T');
		
		map.put("U+00dc", 'U');map.put("U+00fc", 'U');map.put("U+00d9", 'U');map.put("U+00f9", 'U');map.put("U+00da", 'U');map.put("U+00fA", 'U');
		map.put("U+00db", 'U');map.put("U+00fB", 'U');map.put("U+0170", 'U');map.put("U+0171", 'U');map.put("U+0168", 'U');map.put("U+0169", 'U');
		map.put("U+0172", 'U');map.put("U+0173", 'U');map.put("U+016e", 'U');map.put("U+016f", 'U');
		
		map.put("U+0174", 'W');map.put("U+0175", 'W');
		
		map.put("U+00dd", 'Y');map.put("U+00fd", 'Y');map.put("U+0178", 'Y');map.put("U+00ff", 'Y');map.put("U+0176", 'Y');map.put("U+0177", 'Y');
		
		map.put("U+0179", 'Z');map.put("U+017a", 'Z');map.put("U+017d", 'Z');map.put("U+017e", 'Z');map.put("U+017b", 'Z');map.put("U+017c", 'Z');
	}
}


