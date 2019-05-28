package com.linezh;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.linezh.objs.CodeRule;
import com.linezh.objs.FileCount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Author: line
 * Date: 2019-05-27
 * Time: 22:10
 * Description:
 */
public class CodeParser {
    public static FileCount parserSingleFile(File file, Map<String, CodeRule> map) throws IOException {
        long start = System.currentTimeMillis();
        String fileName = file.getName();
        String[] data = fileName.split("\\.");
        CodeRule codeRule = null;
        if (data.length == 1) {
            codeRule = CodeRule.UNKNOWN;
        } else {
            codeRule = map.getOrDefault(data[data.length - 1], CodeRule.UNKNOWN);
        }
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            FileCount fileCount = new FileCount(codeRule.getLanguage(), 0, 0, 0);
            String line = null;
            boolean isOnMultiLineComments = false;
            CodeRule.CommentRule commentRule = null;
            while ((line = bufferedReader.readLine()) != null) {
                String trimedData = CharMatcher.whitespace().trimFrom(line);
//                String trimedData = line;
                if (isOnMultiLineComments) {
                    fileCount.commentCountIncrement();
                    if (commentRule.getEndRegex().matcher(trimedData).matches()) {
                        isOnMultiLineComments = false;
                        commentRule = null;
                    }
                } else if (Strings.isNullOrEmpty(trimedData)) {
                    fileCount.blankCountIncrement();
                } else {
                    boolean commentMatched = false;
                    for (CodeRule.CommentRule rule : codeRule.getCommentRules()) {
                        if (rule.getStartRegex().matcher(trimedData).matches()) {
                            fileCount.commentCountIncrement();
                            if (!rule.isSingleLine()) {
                                isOnMultiLineComments = true;
                                commentRule = rule;
                            }
                            commentMatched = true;
                            break;
                        }
                    }
                    if (!commentMatched) {
                        fileCount.codeCountIncrement();
                    }
                }
            }
            return fileCount;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw e;
                } finally {
                    if (fileReader != null) {
                        fileReader.close();
                    }

                }
            }
            long end = System.currentTimeMillis();
            System.out.println("filename:" + file.getPath() + " time:" + (end - start));
        }
    }
}
