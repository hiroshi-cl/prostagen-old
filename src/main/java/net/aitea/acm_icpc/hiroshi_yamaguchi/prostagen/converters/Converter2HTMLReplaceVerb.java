package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Image;

import java.util.Map;

public class Converter2HTMLReplaceVerb extends Converter2HTML {

	public Converter2HTMLReplaceVerb(String source, Map<String, Image> map, String verb) throws InconvertibleException {
		super(source, map, verb);
	}

	@Override
	protected String verbatimContainer(String s) throws InconvertibleException {
		return "<" + verb + ">" +
                inline(s.replace("$\\vdots$",":\n:").replace("$\\cdots$","...")).replace("\n", "<br>")
                + "</" + verb + ">\n";
	}
}
