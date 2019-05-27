package com.linezh;

import com.linezh.objs.FileCount;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Author: line
 * Date: 2019-05-27
 * Time: 20:27
 * Description:
 */

public class ClocTest {
    public static void main(String[] args) throws IOException {
//        String path = "/Users/line/workspace/cryptocurrency/test";
//        File file = new File(path);
//        File[] files = file.listFiles();
//        System.out.println(files.length);
//        testMatch();
        testParser();
    }

    public static void testMatch() {
        System.out.println(Pattern.matches("^//.*", "//dfsfsfsf"));
        System.out.println(Pattern.matches("^/\\*.*", "/*dsfsdsfs"));
        System.out.println(Pattern.matches("^/\\*.*\\*/$", "/*dsfsdsfs*/"));
        System.out.println(Pattern.matches("^.*\\*/$", "/*dsfsdsfs*/"));
    }

    public static void testParser() throws IOException {
//        File file = new File("/Users/line/workspace/IdeaProjects/cloc/src/test/java/com/linezh/TestJavaFile.java");
        File file = new File("/Users/line/workspace/IdeaProjects/cloc/src/main/java/com/linezh/objs/CodeRule.java");
        FileCount count = CodeParser.parserSingleFile(file, Cloc.CODE_RULE_MAP);
        System.out.println(count);
    }
}
