package com.github.hiroshi_cl.jag.sg;

import java.util.Map;

public class Converter2TeXReplaceVerb extends Converter2TeX {

	public Converter2TeXReplaceVerb(String source, Map<String, Image> map, String verb) throws InconvertibleException {
		super(source, map, verb);
	}

	@Override
	protected String verbatimContainer(String s) throws InconvertibleException {
		return "\\begin{" + verb + "}" + s + "\\end{" + verb + "}\n";
	}
}
