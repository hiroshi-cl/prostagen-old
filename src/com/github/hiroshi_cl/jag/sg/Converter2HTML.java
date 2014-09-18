package com.github.hiroshi_cl.jag.sg;

import java.util.Map;

import com.github.hiroshi_cl.jag.sg.Converter.InconvertibleException;

public class Converter2HTML extends Converter {

	public Converter2HTML(String source, Map<String, Image> map, String verb) throws InconvertibleException {
		super(source, map, verb);
	}

	@Override
	protected String paragraphBR() throws InconvertibleException {
		return "<br>";
	}

	@Override
	protected String itemizeItem(String s) throws InconvertibleException {
		return wrap("li", s);
	}

	@Override
	protected String itemizeContainer(String s) throws InconvertibleException {
		return wrap("ul", s) + "\n";
	}

	@Override
	protected String enumerateItem(String s) throws InconvertibleException {
		return wrap("li", s);
	}

	@Override
	protected String enumerateContainer(String s) throws InconvertibleException {
		return wrap("ol", s) + "\n";
	}

	@Override
	protected String subsubsection(String s) throws InconvertibleException {
		return wrap("<h3>", s) + "\n";
	}

	@Override
	protected String image(Image img, String name, int scale) throws InconvertibleException {
		return "<img src=\"./IMAGE/" + name + "\" height=\"" + (img.height * scale / 100) + "\" width=\""
				+ (img.width * scale / 100) + "\">";
	}

	@Override
	protected String verbatimContainer(String s) throws InconvertibleException {
		return wrap("pre", s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"));
	}

	@Override
	protected String plain(String s) throws InconvertibleException {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
				.replace("{,}", ",");
	}

	@Override
	protected String align(Align a, String s) throws InconvertibleException {
		switch (a) {
		case LEFT:
			return wrap("left", s) + "\n";
		case RIGHT:
			return wrap("right", s) + "\n";
		case CENTER:
			return wrap("center", s) + "\n";
		}
		throw new InconvertibleException();
	}

	@Override
	protected String strong(String s) throws InconvertibleException {
		return wrap("b", s);
	}

	@Override
	protected String italic(String s) throws InconvertibleException {
		return wrap("i", s);
	}

	@Override
	protected String expression(String s) throws InconvertibleException {
		return wrap(
				"var",
				s.replaceAll("_\\{(.+?)\\}", "<sub>$1</sub>").replaceAll("_(.)", "<sub>$1</sub>")
						.replaceAll("\\^\\{(.+?)\\}", "<sup>$1</sup>").replaceAll("\\^(.)", "<sup>$1</sup>")
						.replace("<=", "&le;").replace(">=", "&ge;").replace("!=", "&ne;").replace("*", "&times;"));
	}

	@Override
	protected String htmlescape(String s) throws InconvertibleException {
		return s;
	}

	@Override
	protected String wrapTable(String s, int columns) {
		return wrap("table", s) + "\n";
	}

	@Override
	protected String wrapTC(String s) {
		return wrap("tc", s);
	}

	@Override
	protected String wrapTR(String s) {
		return wrap("tr", s) + "\n";
	}

	private static String wrap(final String tag, final String s) {
		return String.format("<%s>%s</%s>", tag, s, tag);
	}

	@Override
	protected String wrapParagraph(String s) throws InconvertibleException {
		return wrap("p", s);
	}

	@Override
	protected String descriptionCaption(String s) throws InconvertibleException {
		return wrap("dt", s);
	}

	@Override
	protected String descriptionContainer(String s) throws InconvertibleException {
		return wrap("dl", s) + "\n";
	}

	@Override
	protected String descriptionItem(String s) throws InconvertibleException {
		return wrap("dd", s);
	}
}
