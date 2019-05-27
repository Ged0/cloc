package com.linezh;

import com.linezh.objs.CodeRule;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: line
 * Date: 2019-05-27
 * Time: 20:11
 * Description:
 */
public class Cloc {

    public static final Map<String, CodeRule> CODE_RULE_MAP = new HashMap<>();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a folder or file!");
            return;
        }
        initCodeRule();
        ClocExecutor clocExecutor = new ClocExecutor();
        long start = System.currentTimeMillis();
        clocExecutor.submit(new File(args[0]));
        try {
            clocExecutor.getStopSignal().await();
            while (clocExecutor.getFinishedCount() < clocExecutor.getSubmitCount()) {
                Thread.sleep(3);
            }
            clocExecutor.printResult();
        } catch (InterruptedException e) {
            System.out.println("Error happened when wait for result");
        }
        long end = System.currentTimeMillis();
        System.out.println("spent millis:" + (end - start));
    }

    public static void initCodeRule() {
        CodeRule.CommentRule c1 = new CodeRule.CommentRule(true, "^//.*", null);
        CodeRule.CommentRule c2 = new CodeRule.CommentRule(true, "^/\\*.*\\*/$", null);
        CodeRule.CommentRule c3 = new CodeRule.CommentRule(false, "^/\\*.*", "^.*\\*/$");
        CodeRule javaRule = new CodeRule("Java", new String[]{"java"}, new CodeRule.CommentRule[]{c1, c2, c3});
        CodeRule cRule = new CodeRule("C", new String[]{"c", "h"}, new CodeRule.CommentRule[]{c1, c2, c3});

        addRuleToCodeMap(javaRule);
        addRuleToCodeMap(cRule);
    }

    private static void addRuleToCodeMap(CodeRule codeRule) {
        for (String extension : codeRule.getExtensions()) {
            CODE_RULE_MAP.put(extension, codeRule);
        }
    }

}
