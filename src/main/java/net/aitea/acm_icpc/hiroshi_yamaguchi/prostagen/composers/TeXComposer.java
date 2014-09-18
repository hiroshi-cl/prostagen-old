package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.composers;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters.Converter;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters.Converter2TeX;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.converters.Converter2TeXReplaceVerb;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Downloader;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Image;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Writer;

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
        sb.append("\\Output\n");
        sb.append(new Converter2TeXReplaceVerb(sc.getSection(SectionNames.output), map, "InputFormat").get());
        sb.append("\\Sample\n");

        final List<String> sampleInputs = sc.getSections(SectionNames.sampleInput, SectionNames.sampleInputF);
        final List<String> sampleOutputs = sc.getSections(SectionNames.sampleOutput, SectionNames.sampleOutputF);

        final StringBuilder sampleInput = new StringBuilder();
        for (final String s : sampleInputs)
            sampleInput.append(s);
        final StringBuilder sampleOutput = new StringBuilder();
        for (final String s : sampleOutputs)
            sampleOutput.append(s);

        sb.append(new Converter2TeXReplaceVerb(sampleInput.toString().replace("\n\n", "\n"), map, "SampleInput").get());
        sb.append(new Converter2TeXReplaceVerb(sampleOutput.toString().replace("\n\n", "\n"), map, "SampleOutput").get());

        if(sc.hasSection("Note")) {
            sb.append("\\Note\n");
            sb.append(new Converter2TeX(sc.getSection("Note"), map, null).get());
        }

        Writer.writeString(new File("tex", id + ".tex"), sb.toString());
        Writer.writeImages(new File("tex", "fig"), map);
    }
}
