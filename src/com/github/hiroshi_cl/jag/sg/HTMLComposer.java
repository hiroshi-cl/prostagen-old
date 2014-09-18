package com.github.hiroshi_cl.jag.sg;

import java.io.File;
import java.util.List;
import java.util.Map;

public class HTMLComposer {
    public static void compose(final Downloader d, final String pageName, final String id)
            throws Converter.InconvertibleException {
        final String source = d.getPage(pageName);
        final Map<String, Image> map = d.getImages(pageName, source);
        final Sectionizer sc = new Sectionizer(source);

        final StringBuilder sb = new StringBuilder();

        sb.append("<h1>" + sc.getSection(SectionNames.title).trim() + "</h1>\n");
//		System.err.println(sc.getSection(SectionNames.statement));
        sb.append(new Converter2HTML(sc.getSection(SectionNames.statement), map, null).get());
        sb.append("<h2>Input</h2>\n");
        sb.append(new Converter2HTMLReplaceVerb(sc.getSection(SectionNames.input), map, "blockquote").get());

        sb.append("<h3>Constraints</h3>\n");
        sb.append(new Converter2HTMLReplaceVerb(sc.getSection(SectionNames.constraints), map, "blockquote").get());

        sb.append("<h2>Output</h2>\n");
        sb.append(new Converter2HTMLReplaceVerb(sc.getSection(SectionNames.output), map, "blockquote").get());

        final List<String> sampleInputs = sc.getSections(SectionNames.sampleInput, SectionNames.sampleInputF);
        final List<String> sampleOutputs = sc.getSections(SectionNames.sampleOutput, SectionNames.sampleOutputF);

        final int num = sampleInputs.size();
        if (sampleOutputs.size() != num)
            throw null;
        for (int i = 0; i < num; i++) {
            sb.append("<h2>Sample Input " + (i + 1) + "</h2>\n\n");
            sb.append(new Converter2HTML(sampleInputs.get(i), map, null).get());
            sb.append("<h2>Output for the Sample Input " + (i + 1) + "</h2>\n\n");
            sb.append(new Converter2HTML(sampleOutputs.get(i), map, null).get());
        }
        Writer.writeString(new File("html/" + id, id + ".html"), sb.toString());
        Writer.writeImages(new File("html/" + id, "IMAGE"), map);
    }
}
