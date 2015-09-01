package es.util;

/**
 * Created by Johannes on 31/08/2015.
 */
public class Quality {

	public static int function(String a, String b) {
		return custom(a,b);
	}

	public static int distance(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		// i == 0
		int [] costs = new int [b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

	public static int custom(String candidate, String target) {
		candidate = candidate.toLowerCase();
		target = target.toLowerCase();
		char[] aChars = candidate.toCharArray();
		char[] bChars = target.toCharArray();

		int quality = 0;
		for(int i = 0; i < candidate.length(); i++) {
			int diff = Math.abs(aChars[i] - bChars[i]);
			float limit = Math.round(('z' - 'a')/2.0);
			if(diff > limit)
				quality += ('z' - 'a') - diff;
			else
				quality += diff;
		}
		return quality;
	}
}
