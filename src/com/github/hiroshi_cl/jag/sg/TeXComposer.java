package com.github.hiroshi_cl.jag.sg;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TeXComposer {
    public static void compose(final Downloader d, final String pageName, final String id)
            throws Converter.InconvertibleException {
        final String source = d.getPage(pageName);
        final Map<String, Image> map = d.getImages(pageName, source);
        final Sectionizer sc = new Sectionizer(source);

        final StringBuilder sb = new StringBuilder();
        sb.append("\\Problem{" + sc.getSection(SectionNames.title).trim() + "}\n");
        // System.err.println(sc.getSection(SectionNames.statement));
        sb.append(new Converter2TeX(sc.getSection(SectionNames.statement), map, null).get());
        sb.append("\\Input\n");
        sb.append(new Converter2TeXReplaceVerb(sc.getSection(SectionNames.input), map, "InputFormat").get());

        sb.append("\\Constraints\n");
        sb.append(new Converter2TeXReplaceVerb(sc.getSection(SectionNames.constraints), map, "InputFormat").get());

        sb.append("\\Output\n");
        sb.append(new Converter2TeXReplaceVerb(sc.getSection(SectionNames.output), map, "InputFormat").get());
        sb.append("\\Sample\n");

        final List<String> sampleInputs = sc.getSections(SectionNames.sampleInput, SectionNames.sampleInputF);
        final List<String> sampleOutputs = sc.getSections(SectionNames.sampleOutput, SectionNames.sampleOutputF);

        final int num = sampleInputs.size();
        if (sampleOutputs.size() != num) {
            System.err.println(sampleInputs + "\n" + sampleOutputs);
            throw null;
        }
        for (int i = 0; i < num; i++) {
            sb.append(new Converter2TeXReplaceVerb(sampleInputs.get(i), map, "SampleInput").get());
            sb.append(new Converter2TeXReplaceVerb(sampleOutputs.get(i), map, "SampleOutput").get());
        }
        Writer.writeString(new File("tex", id + ".tex"), sb.toString());
        Writer.writeImages(new File("tex", "fig"), map);
    }
}
