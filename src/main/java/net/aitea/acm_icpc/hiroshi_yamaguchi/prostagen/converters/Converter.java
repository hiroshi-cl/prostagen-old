package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Image;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Converter {
    protected final Map<String, Image> map;
    protected final String verb;
    private final StringBuilder sb = new StringBuilder();
    private char[] cs;
    private int c = 0;

    public Converter(final String source, final Map<String, Image> map, String verb) throws InconvertibleException {
        cs = (source + '\n').toCharArray();
//        System.err.println(cs);
        this.map = map;
        this.verb = verb;
        parse();
    }

    private static int lcnt(final char[] cs, final char c, final int offset) {
        int ret = 0;
        for (int i = offset; i < cs.length && cs[i] == c; i++)
            ret++;
        return ret;
    }

    private void parse() throws InconvertibleException {
        while (c < cs.length) {
            while (comment()) ;
            sb.append(block());
        }
    }

    private boolean comment() throws InconvertibleException {
        if (cs[c] != '/' || cs[c + 1] != '/')
            return false;
        readLine();
        return true;
    }

    private String block() throws InconvertibleException {
        final int t = c;
        final String s = nonParagraphBlock();
        if (s == null) {
            c = t;
            return paragraph();
        }
        return s;
    }

    private String nonParagraphBlock() throws InconvertibleException {
        switch (cs[c]) {
            case '\n':
                return empty();
            case '>':
            case '<':
                return quote();
            case '-':
                return itemize();
            case '+':
                return enumerate();
            case '*':
                return section();
            case '#':
                return blockPlugin();
            case '|':
                return table();
            case ':':
                return description();
            case ' ':
                return verbatim();
            default:
                return null;
        }
    }

    private String empty() throws InconvertibleException {
        c++;
        return "\n";
    }

    private String rawParagraph() throws InconvertibleException {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            if (cs[c] == '~')
                c++;
            while (comment())
                ;
            final String line = readLine();
            if (line.endsWith("~")) {
                sb.append(inline(line.substring(0, line.length() - 1))).append('\n');
            } else {
                sb.append(inline(line)).append('\n');
                final int t = c;
                final String s = nonParagraphBlock();
                c = t;
                if (s != null)
                    break;
            }
        }
        return sb.toString();
    }

    private String paragraph() throws InconvertibleException {
        return wrapParagraph(rawParagraph());
    }

    protected abstract String wrapParagraph(String s) throws InconvertibleException;

    protected abstract String paragraphBR() throws InconvertibleException;

    private String quote() throws InconvertibleException {
        throw new InconvertibleException();
//        final int level = lcnt(cs, '>', c);
//        final StringBuilder container = new StringBuilder();
//        while (lcnt(cs, '>', c) == level) {
//            final StringBuilder item = new StringBuilder();
//            c += level;
//            if (c >= cs.length) {
//                container.append(quoteItem(item.toString()));
//                break;
//            }
//            if (cs[c] == '\n')
//                item.append(block());
//            else
//                item.append(paragraph());
//            if (lcnt(cs, '<', c) > level) {
//                unquote();
//                container.append(quoteItem(item.toString()));
//                break;
//            }
//            while (lcnt(cs, '>', c) > level)
//                item.append(quote());
//            container.append(quoteItem(item.toString()));
//        }
//        return quoteContainer(container.toString());
    }

    protected abstract String quoteItem(final String s) throws InconvertibleException;

    protected abstract String quoteContainer(final String s) throws InconvertibleException;

    private String itemize() throws InconvertibleException {
        final int level = lcnt(cs, '-', c);
        final StringBuilder container = new StringBuilder();
        while (lcnt(cs, '-', c) == level) {
            final StringBuilder item = new StringBuilder();
            c += level;
            if (cs[c] == '\n')
                item.append(block());
            else
                item.append(rawParagraph());
            while (lcnt(cs, '-', c) > level)
                item.append(itemize());
            container.append(itemizeItem(item.toString()));
        }
        return itemizeContainer(container.toString());
    }

    protected abstract String itemizeItem(String s) throws InconvertibleException;

    protected abstract String itemizeContainer(String s) throws InconvertibleException;

    private String enumerate() throws InconvertibleException {
        final int level = lcnt(cs, '+', c);
        final StringBuilder container = new StringBuilder();
        while (lcnt(cs, '+', c) == level) {
            final StringBuilder item = new StringBuilder();
            c += level;
            if (cs[c] == '\n')
                item.append(block());
            else
                item.append(rawParagraph());
            while (lcnt(cs, '+', c) > level)
                item.append(enumerate());
            container.append(enumerateItem(item.toString()));
        }
        return enumerateContainer(container.toString());
    }

    protected abstract String enumerateItem(String s) throws InconvertibleException;

    protected abstract String enumerateContainer(String s) throws InconvertibleException;

    private String section() throws InconvertibleException {
        c += 3;
        return subsubsection(readLine().replaceAll("\\[.+?\\]", "").trim());
    }

    protected abstract String subsubsection(String s) throws InconvertibleException;

    private String blockPlugin() throws InconvertibleException {
        final String line = readLine();
        final Matcher m = Pattern.compile("#ref\\((?:&quot;)?([^,]+?)(?:,.+)?(?:&quot;)?\\)").matcher(line);
        if (m.matches()) {
            final String name = m.group(1);
            final String scale = m.groupCount() < 2 ? null : m.group(2);
            if (map.containsKey(name))
                return image(map.get(name), name, scale == null ? 100 : Integer.parseInt(scale));
        }
        System.err.println("Sorry! \"" + line + "\" is inconvertible plugin.");
        throw new InconvertibleException();
    }

    protected abstract String image(Image img, String name, int scale) throws InconvertibleException;

    private String description() throws InconvertibleException {
        final int level = lcnt(cs, ':', c);
        final StringBuilder container = new StringBuilder();
        while (lcnt(cs, ':', c) == level) {
            {
                c += level;
                final StringBuilder caption = new StringBuilder();
                while (cs[c] != '|') {
                    caption.append(cs[c++]);
                    if (cs[c] == '\n')
                        return null;
                }
                container.append(descriptionCaption(inline(caption.toString())));
                c++;
            }
            {
                final StringBuilder item = new StringBuilder();
                if (cs[c] == '\n')
                    item.append(block());
                else
                    item.append(rawParagraph());
                while (lcnt(cs, ':', c) > level)
                    item.append(description());
                container.append(descriptionItem(item.toString()));
            }
        }
        return descriptionContainer(container.toString());
    }

    protected abstract String descriptionContainer(String s) throws InconvertibleException;

    protected abstract String descriptionCaption(String s) throws InconvertibleException;

    protected abstract String descriptionItem(String s) throws InconvertibleException;

    protected static enum TableMode {
        Head, Body, Foot, Config;

        static TableMode tableMode(final String column) throws InconvertibleException {
            switch (column) {
                case "cb":
                    return Config;
                case "hb":
                    return Head;
                case "fb":
                    return Foot;
                case "b":
                    return Body;
                default:
                    throw new InconvertibleException();
            }
        }
    }

    private String table() throws InconvertibleException {
        final String first = readLine();
        if (!"cfh|".contains(first.substring(first.length() - 1)))
            return null;
        String[] ss = (first + "b").substring(1).split("\\|");
        final int columns = ss.length - 1;
        final StringBuilder head = new StringBuilder();
        final StringBuilder body = new StringBuilder();
        final StringBuilder foot = new StringBuilder();
        for (String line = first; ; ) {
            final TableMode mode = TableMode.tableMode(ss[columns]);
            if (mode == TableMode.Config)
                throw new InconvertibleException();
            final StringBuilder linesb = new StringBuilder();
            for (int i = 0; i < columns; i++)
                if (ss[i].startsWith("~"))
                    linesb.append(wrapTH(ss[i].substring(1)));
                else
                    linesb.append(wrapTC(ss[i]));
            final String wrappedLine = wrapTR(linesb.toString());
            switch (mode) {
                case Head: {
                    head.append(wrappedLine);
                    break;
                }
                case Body: {
                    body.append(wrappedLine);
                    break;
                }
                case Foot: {
                    foot.append(wrappedLine);
                    break;
                }
            }
            final int t = c;
            line = readLine();
            if (line.isEmpty() || !"cfh|".contains(line.substring(line.length() - 1))) {
                c = t;
                break;
            }
            ss = (line + "b").substring(1).split("\\|");
            if (ss.length - 1 != columns) {
                c = t;
                break;
            }
        }
        return wrapTable(
                wrapTHead(head.toString()) + wrapTBody(body.toString()) + wrapTFoot(foot.toString()),
                columns);
    }

    protected abstract String wrapTable(final String s, final int columns) throws InconvertibleException;

    protected abstract String wrapTHead(final String s) throws InconvertibleException;

    protected abstract String wrapTBody(final String s) throws InconvertibleException;

    protected abstract String wrapTFoot(final String s) throws InconvertibleException;

    protected abstract String wrapTR(final String s) throws InconvertibleException;

    protected abstract String wrapTC(final String s) throws InconvertibleException;

    protected abstract String wrapTH(final String s) throws InconvertibleException;

    private String verbatim() throws InconvertibleException {
        final StringBuilder sb = new StringBuilder();
        while (cs[c] == ' ') {
            c++;
            if (sb.length() > 0)
                sb.append('\n');
            sb.append(readLine());
        }
        return verbatimContainer(sb.toString());
    }

    protected abstract String verbatimContainer(String s) throws InconvertibleException;

    protected final String inline(String s) throws InconvertibleException {
        if (s.contains(":")) {
            final String[] ss = s.split(":", 2);
            try {
                final Align a = Align.valueOf(ss[0]);
                return align(a, inline(ss[1]));
            } catch (IllegalArgumentException e) {
            }
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.startsWith("%%%", i) && s.substring(i + 3).contains("%%%")) {
                final String[] ss = s.substring(i + 3).split("%%%", 2);
                return plain(s.substring(0, i)) + underbar(inline(ss[0])) + inline(ss[1]);
            }
            if (s.startsWith("%%", i) && s.substring(i + 2).contains("%%")) {
                final String[] ss = s.substring(i + 2).split("%%", 2);
                return plain(s.substring(0, i)) + strike(inline(ss[0])) + inline(ss[1]);
            }
            if (s.startsWith("'''", i) && s.substring(i + 3).contains("'''")) {
                final String[] ss = s.substring(i + 3).split("'''", 2);
                return plain(s.substring(0, i)) + italic(inline(ss[0])) + inline(ss[1]);
            }
            if (s.startsWith("''", i) && s.substring(i + 2).contains("''")) {
                final String[] ss = s.substring(i + 2).split("''", 2);
                return plain(s.substring(0, i)) + strong(inline(ss[0])) + inline(ss[1]);
            }
            if (s.startsWith("((", i) && s.substring(i + 2).contains("))")) {
                final String[] ss = s.substring(i + 2).split("\\)\\)", 2);
                return plain(s.substring(0, i)) + annotation(inline(ss[0])) + inline(ss[1]);
            }
            if (s.startsWith("$", i) && s.substring(i + 1).contains("$")) {
                final String[] ss = s.substring(i + 1).split("\\$", 2);
                return plain(s.substring(0, i)) + expression(ss[0]) + inline(ss[1]);
            }
            if (s.startsWith("&", i) && s.substring(i + 1).contains(";")) {
                final String[] ss = s.substring(i + 1).split(";", 2);
                return plain(s.substring(0, i)) + inlinePlugin(ss[0]) + inline(ss[1]);
            }
        }
        return plain(s);
    }

    protected abstract String plain(String s) throws InconvertibleException;

    protected abstract String align(Align a, String s) throws InconvertibleException;

    protected String strike(String s) throws InconvertibleException {
        return "";
    }

    protected String underbar(String s) throws InconvertibleException {
        return s;
    }

    protected String annotation(String s) throws InconvertibleException {
        return "";
    }

    protected abstract String strong(String s) throws InconvertibleException;

    protected abstract String italic(String s) throws InconvertibleException;

    protected abstract String expression(String s) throws InconvertibleException;

    protected final String inlinePlugin(String s) throws InconvertibleException {
        final Matcher m = Pattern.compile("ref\\((?:&quot;)?([^,]+?)(?:,([^,]+)*%)?(?:&quot;)?\\)").matcher(s);
        if (m.matches()) {
            final String name = m.group(1);
            final String scale = m.group(2);
            if (map.containsKey(name))
                return image(map.get(name), name, scale == null ? 100 : Integer.parseInt(scale));
        }
        return htmlescape(s);
    }

    protected abstract String htmlescape(String s) throws InconvertibleException;

    private String readLine() {
        final StringBuilder sb = new StringBuilder();
        while (cs[c] != '\n')
            sb.append(cs[c++]);
        c++;
        return sb.toString();
    }

    public final String get() {
        return sb.toString();
    }

    protected static enum Align {
        LEFT, RIGHT, CENTER
    }

    public static class InconvertibleException extends Exception {
        private static final long serialVersionUID = -3391878470041902595L;
    }
}
