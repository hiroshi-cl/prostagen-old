package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Image;

import java.util.Map;

public class Converter2HTML extends Converter {

    public Converter2HTML(String source, Map<String, Image> map, String verb) throws InconvertibleException {
        super(source, map, verb);
    }

    private static String wrap(final String tag, final String s) {
        return String.format("<%s>%s</%s>", tag, s, tag);
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
        return wrap("h3", s.trim()) + "\n";
    }

    @Override
    protected String image(Image img, String name, int scale) throws InconvertibleException {
        throw new InconvertibleException();
//        return "<img src=\"/img/other/jag2013_autumn/" + name
//                + "\" height=\"" + (img.height * scale / 100)
//                + "\" width=\"" + (img.width * scale / 100) + "\" />";
    }

    @Override
    protected String verbatimContainer(String s) throws InconvertibleException {
        return wrap("pre", s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"));
    }

    @Override
    protected String plain(String s) throws InconvertibleException {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
                .replaceAll("&quot;(.+?)&quot;", "\"<samp>$1</samp>\"").replaceAll("'(.)'", "'<samp>$1</samp>'")
                .replaceAll("\\\\textit\\{(.+?)\\}", "<i>$1</i>")
                .replaceAll("\\\\textbf\\{(.+?)\\}", "<b>$1</b>");
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
        return "$" + s + "$";
    }

    @Override
    protected String htmlescape(String s) throws InconvertibleException {
        return s;
    }

    @Override
    protected String wrapTable(String s, int columns) {
        return String.format("<table border=\"1\">%s</table>", s) + "\n";
    }

    @Override
    protected String wrapTHead(String s) throws InconvertibleException {
        if (s.isEmpty())
            return "";
        else
            return wrap("thead", s) + "\n";
    }

    @Override
    protected String wrapTBody(String s) throws InconvertibleException {
        if (s.isEmpty())
            throw new InconvertibleException();
        else
            return wrap("tbody", s) + "\n";
    }

    @Override
    protected String wrapTFoot(String s) throws InconvertibleException {
        if (s.isEmpty())
            return "";
        else
            return wrap("tfoot", s) + "\n";
    }

    @Override
    protected String wrapTC(String s) {
        return wrap("td", s.trim());
    }

    @Override
    protected String wrapTH(String s) {
        return wrap("th", s.trim());
    }

    @Override
    protected String wrapTR(String s) {
        return wrap("tr", s) + "\n";
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

    @Override
    protected String quoteContainer(String s) throws InconvertibleException {
        return wrap("blockquote", s);
    }

    @Override
    protected String quoteItem(String s) throws InconvertibleException {
        return s + "\n";
    }
}
