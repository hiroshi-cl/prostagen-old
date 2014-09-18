package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.composers;

import java.util.*;

public class Sectionizer {
	private final NavigableMap<String, String> map = new TreeMap<String, String>();

	public Sectionizer(final String s) {
		final Scanner sc = new Scanner(s);
		String sectionName = "";
		final StringBuilder section = new StringBuilder();
		while (sc.hasNextLine()) {
			final String line = sc.nextLine();
			if (line.matches("\\*[^*].*"))
				map.put("Title", line.substring(1).replaceAll("\\[.+?\\]", "").replaceAll("\\(.+?\\)", "").trim());
			if (line.startsWith("----") || line.matches("\\*+[^*].*")) {
				map.put(sectionName.toString(), section.toString());
				sectionName = line.matches("\\*+[^*].*") ? line.substring(2).replaceAll("\\[.+?\\]", "").trim() : "";
				section.delete(0, section.length());
			} else
				section.append(line).append('\n');
		}
		map.put(sectionName.toString(), section.toString());
	}

	public String getSection(final String sectionName) {
		if (!map.containsKey(sectionName))
			throw null;
		return map.get(sectionName);
	}

	public boolean hasSection(final String sectionName) {
		return map.containsKey(sectionName);
	}

	public List<String> getSections(final String sectionNamePrefix, final String regex) {
		final List<String> list = new ArrayList<String>();
		for (final String key : map.tailMap(sectionNamePrefix).keySet()) {
			if (!key.startsWith(sectionNamePrefix))
				break;
			if (key.matches(regex))
				list.add(map.get(key));
		}
		return list;
	}

	public Set<String> getSectionNames() {
		return map.keySet();
	}
}
