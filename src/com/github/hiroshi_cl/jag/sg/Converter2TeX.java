package com.github.hiroshi_cl.jag.sg;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter2TeX extends Converter {

	public Converter2TeX(String source, Map<String, Image> map, String verb) throws InconvertibleException {
		super(source, map, verb);
	}

	@Override
	protected String paragraphBR() throws InconvertibleException {
		return "\\\\";
	}

	@Override
	protected String itemizeItem(String s) throws InconvertibleException {
		return "\\item " + s;
	}

	@Override
	protected String itemizeContainer(String s) throws InconvertibleException {
		return "\\begin{itemize}\n" + s + "\\end{itemize}\n";
	}

	@Override
	protected String enumerateItem(String s) throws InconvertibleException {
		return "\\item " + s;
	}

	@Override
	protected String enumerateContainer(String s) throws InconvertibleException {
		return "\\begin{enumerate}\n" + s + "\\end{enumerate}\n";
	}

	@Override
	protected String image(Image img, String name, int scale) throws InconvertibleException {
		StringBuilder sb = new StringBuilder();
		sb.append("\\begin{figure}[htbp]").append('\n');
		sb.append("\\begin{center}").append('\n');
		sb.append("\\vspace{+\\baselineskip}").append('\n');
		sb.append("\\includegraphics[width=150mm,bb=0 0 ").append(img.width).append(' ').append(img.height)
				.append("]{fig/").append(name).append("}").append('\n');
		sb.append("\\end{center}").append('\n');
		// sb.append("\\caption{caption}").append('\n');
		sb.append("\\end{figure}").append('\n');
		return sb.toString();
	}

	@Override
	protected String verbatimContainer(String s) throws InconvertibleException {
		return "\\begin{verbatim}\n" + s + "\\end{verbatim}\n";
	}

	@Override
	protected String plain(String s) throws InconvertibleException {
		return s.replace("\\", "\\textbackslash ").replace("{", "\\{").replace("}", "\\}").replace("%", "\\%")
				.replace("#", "\\#").replace("^", "\\^").replace("_", "\\_").replace("&", "\\&")
				.replace("<", "\\texttt{<}").replace(">", "\\texttt{>}");
	}

	@Override
	protected String align(Align a, String s) throws InconvertibleException {
		switch (a) {
		case LEFT:
			return "\\begin{flushleft}\n" + s + "\\end{flushleft}\n";
		case RIGHT:
			return "\\begin{flushright}\n" + s + "\\end{flushright}\n";
		case CENTER:
			return "\\begin{center}\n" + s + "\\end{center}\n";
		}
		throw new InconvertibleException();
	}

	@Override
	protected String strong(String s) throws InconvertibleException {
		return "\\\\textbf{" + s + "}";
	}

	@Override
	protected String italic(String s) throws InconvertibleException {
		return "\\\\textit{" + s + "}";
	}

	@Override
	protected String expression(String s) throws InconvertibleException {
		return "$" + s.replace("<=", "\\le ").replace(">=", "\\ge ").replace("!=", "\\ne ").replace("*", "\\times ")
				+ "$";
	}

	@Override
	protected String htmlescape(String s) throws InconvertibleException {
		System.err.println(s);
		if (s.startsWith("#"))
			return "" + (char) toInt(s.substring(1));
        final Matcher m = Pattern.compile("color\\((.+?)\\)\\{(.+?)\\}").matcher(s);
        if(m.matches()) {
            return "<span style=\"color:" + m.group(1) +"\">" + m.group(2) + "</span>";
        }
		throw new InconvertibleException();
	}

	private static int toInt(String s) {
		if (s.startsWith("x"))
			return Integer.parseInt(s.substring(1), 16);
		return Integer.parseInt(s);
	}

	@Override
	protected String subsubsection(String s) throws InconvertibleException {
		return "\\SubSection{" + s + "}\n";
	}

	@Override
	protected String wrapTable(String s, int columns) {
		final StringBuilder sb = new StringBuilder("|");
		for (int i = 0; i < columns; i++)
			sb.append("l|");
		return "\\begin{table}{" + sb.toString() + "}\n" + s + "\\end{table}\n";
	}

	@Override
	protected String wrapTC(String s) {
		return "&" + s;
	}

	@Override
	protected String wrapTR(String s) {
		return s.substring(1) + "\\\\";
	}

	@Override
	protected String wrapParagraph(String s) throws InconvertibleException {
		return "\n" + s + "\n";
	}

	@Override
	protected String descriptionCaption(String s) {
		return "\\item[" + s + "]";
	}

	@Override
	protected String descriptionContainer(String s) {
		return "\\begin{description}\n" + s + "\\end{description}\n";
	}

	@Override
	protected String descriptionItem(String s) {
		return s + "\n";
	}
}
