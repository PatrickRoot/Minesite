package tech.minesoft.mine.site.tools;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.ShellRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DbTableTools {
    private static String tpl = "<table tableName=\"TPH\" domainObjectName=\"CPH\" enableCountByExample=\"false\"\n" +
            "               enableUpdateByExample=\"false\" enableDeleteByExample=\"false\" enableSelectByExample=\"false\"\n" +
            "               selectByExampleQueryId=\"false\">\n" +
            "            <generatedKey column=\"ID\" sqlStatement=\"MySql\" identity=\"true\"/>\n" +
            "        </table>";

    public static void main(String[] args) throws Exception {
        String[] tables = new String[]{
                "ms_spider_content_xxxxx",
                "ms_spider_fields",
                "ms_spider_jobs",
                "ms_spider_urls",
        };

        // 获取到原始generatorConfig
        ClassLoader classLoader = DbTableTools.class.getClassLoader();
        URL url = classLoader.getResource("generatorConfig.xml");
        File file = new File(url.getFile());

        List<String> lines = IOUtils.readLines(new FileInputStream(file), StandardCharsets.UTF_8);

        // 替换目标目录
        File parent = file.getParentFile().getParentFile().getParentFile();
        String parentPath = parent.getAbsolutePath();
        for (int i = 0; i < lines.size(); i++) {
            String temp = lines.get(i);
            if(temp.contains("PPH")){
                temp = StringUtils.replace(temp, "PPH", parentPath);
                lines.set(i, temp);
            }
        }

        // 循环 table 名，替换
        for (String table : tables) {
            String clz = camel(table);

            String temp = StringUtils.replace(tpl, "TPH", table);
            temp = StringUtils.replace(temp, "CPH", clz);

            lines.add(lines.size() - 2, temp);
        }

        // 写入配置文件
        File out = file.getParentFile().toPath().resolve("generator.xml").toFile();
        OutputStream os = new FileOutputStream(out);
        IOUtils.writeLines(lines, "\n", os, StandardCharsets.UTF_8);

        // 调用
        args = new String[]{
                "-configfile",
                out.getAbsolutePath(),
                "-overwrite",
                "-verbose",
        };
        ShellRunner.main(args);
    }

    public static String camel(String table) {
        String[] words = StringUtils.split(table, "_");

        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(StringUtils.capitalize(word.toLowerCase()));
        }

        return sb.toString();
    }
}
