package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.composers;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters.Converter;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters.Converter2HTML;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters.Converter2HTMLReplaceVerb;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Downloader;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Image;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class DomesticComposer {
    public static void compose(final Downloader d, final String pageName, final String id)
            throws Converter.InconvertibleException {
        final String source = d.getPage(pageName);
//        final Map<String, Image> map = d.getImages(pageName, source);
        final Sectionizer sc = new Sectionizer(source);

        final StringBuilder sb = new StringBuilder();

        sb.append("<script type=\"text/x-mathjax-config\">\n" +
                "\tMathJax.Hub.Config({ tex2jax: { inlineMath: [[\"$\",\"$\"], [\"\\\\(\",\"\\\\)\"]], processEscapes: true }});\n" +
                "</script>\n" +
                "<script type=\"text/javascript\"\n" +
                "\tsrc=\"http://acm-icpc.aitea.net/files/mathjax/MathJax.js?config=TeX-AMS_HTML\">\n" +
                "</script>\n" +
                "<meta http-equiv=\"X-UA-Compatible\" CONTENT=\"IE=EmulateIE7\" />").append('\n');

        final String[] titles = sc.getSection(SectionNames.title).trim().split("\n\n");
        sb.append("<!-- begin en only -->").append('\n');
        sb.append(String.format("<h3><u>%s</u></h3>", titles[1])).append('\n');
        sb.append("<!-- end en only -->").append('\n');

        sb.append("<!-- begin ja only -->").append('\n');
        sb.append(String.format("<h3><u>%s</u></h3>", titles[0])).append('\n');
        sb.append("<!-- end ja only -->").append('\n');


        // problem statement section
        sb.append("<div>").append('\n');
        sb.append("<!-- begin en only -->").append('\n');
        sb.append("<p>").append('\n');
        sb.append("English text is not available in this practice contest.").append('\n');
        sb.append("</p>").append('\n');
        sb.append("<!-- end en only -->").append('\n');
        sb.append("<!-- begin ja only -->").append('\n');
        sb.append(new Converter2HTML(sc.getSection(SectionNames.statement), null, null).get()).append('\n');
        sb.append("<!-- end ja only -->").append('\n');
        sb.append("</div>").append('\n');


        // input section
        sb.append("<h3>Input</h3>").append('\n');
        sb.append("<div>").append('\n');
        sb.append("<!-- begin ja only -->").append('\n');
        sb.append(new Converter2HTMLReplaceVerb(sc.getSection(SectionNames.input), null, "blockquote").get()).append('\n');
        sb.append("<!-- end ja only -->").append('\n');
        sb.append("</div>").append('\n');

        // output section
        sb.append("<h3>Output</h3>").append('\n');
        sb.append("<div>").append('\n');
        sb.append("<!-- begin ja only -->").append('\n');
        sb.append(new Converter2HTMLReplaceVerb(sc.getSection(SectionNames.output), null, "blockquote").get()).append('\n');
        sb.append("<!-- end ja only -->").append('\n');
        sb.append("</div>").append('\n');

        final BufferedReader sampleInputs = new BufferedReader(
                new StringReader(sc.getSection(SectionNames.sampleInput)));
        final BufferedReader sampleOutputs = new BufferedReader(
                new StringReader(sc.getSection(SectionNames.sampleOutput)));

        // sample input section & sample output section
        try {
            sb.append("<h3>Sample Input</h3>").append('\n');
            sb.append("<div>").append('\n');
            final StringBuilder sampleInput = new StringBuilder();
            for (String line = sampleInputs.readLine(); line != null; line = sampleInputs.readLine())
                if (!line.isEmpty() && line.charAt(0) == ' ')
                    sampleInput.append(line).append('\n');
            sb.append(new Converter2HTML(sampleInput.toString().replace("\n\n", "\n"), null, null).get()).append('\n');
            sb.append("</div>");
            sb.append("<h3>Output for Sample Input</h3>").append('\n');
            sb.append("<div>").append('\n');
            final StringBuilder sampleOutput = new StringBuilder();
            for (String line = sampleOutputs.readLine(); line != null; line = sampleOutputs.readLine())
                if (!line.isEmpty() && line.charAt(0) == ' ')
                    sampleOutput.append(line).append('\n');
            sb.append(new Converter2HTML(sampleOutput.toString().replace("\n\n", "\n"), null, null).get()).append('\n');
            sb.append("</div>");
        } catch (IOException e) {
            e.printStackTrace();
            throw new Converter.InconvertibleException();
        }
        sb.append("</div>").append('\n');

        Writer.writeString(new File("contest/", id + ".html"), sb.toString());
//        Writer.writeImages(new File("html/", "img"), map);
    }
}
