package com.linezh.objs;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Author: line
 * Date: 2019-05-27
 * Time: 20:53
 * Description:
 */
public class CodeRule {
    private String language;
    private String[] extensions;
    private CommentRule[] commentRules;

    public static CodeRule UNKNOWN = new CodeRule("Unknown", new String[]{}, new CommentRule[]{});


    public CodeRule(String language, String[] extensions, CommentRule[] commentRules) {
        this.language = language;
        this.extensions = extensions;
        this.commentRules = commentRules;
        Arrays.sort(this.commentRules, (a, b) -> {
            if (a.isSingleLine == b.isSingleLine) {
                return 0;
            } else if (a.isSingleLine) {
                return -1;
            } else {
                return 1;
            }
        });
    }

    public String getLanguage() {
        return language;
    }

    public CodeRule setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public CommentRule[] getCommentRules() {
        return commentRules;
    }

    public static class CommentRule {
        private boolean isSingleLine;
        private Pattern startRegex;
        private Pattern endRegex;

        public CommentRule(boolean isSingleLine, String startRegex, String endRegex) {
            this.isSingleLine = isSingleLine;
            this.startRegex = Pattern.compile(startRegex);
            if (!this.isSingleLine) {
                this.endRegex = Pattern.compile(endRegex);
            }
        }

        public boolean isSingleLine() {
            return isSingleLine;
        }

        public Pattern getStartRegex() {
            return startRegex;
        }

        public Pattern getEndRegex() {
            return endRegex;
        }
    }
}
