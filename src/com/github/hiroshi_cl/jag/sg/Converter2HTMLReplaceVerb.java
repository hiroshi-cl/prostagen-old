package com.github.hiroshi_cl.jag.sg;

import java.util.Map;

public class Converter2HTMLReplaceVerb extends Converter2HTML {

	public Converter2HTMLReplaceVerb(String source, Map<String, Image> map, String verb) throws InconvertibleException {
		super(source, map, verb);
	}

	@Override
	protected String verbatimContainer(String s) throws InconvertibleException {
		return "<" + verb + ">\n" + inline(s).replace("\n", "<br>") + "</" + verb + ">\n";
	}
}
