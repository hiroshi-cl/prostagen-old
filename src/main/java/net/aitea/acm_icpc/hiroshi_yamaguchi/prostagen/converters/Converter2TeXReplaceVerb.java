package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Image;

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
