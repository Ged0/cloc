package com.linezh.objs;

/**
 * Author: line
 * Date: 2019-05-27
 * Time: 22:22
 * Description:
 */
public class FileCount {
    private String language;
    private long blankCount;
    private long commentsCount;
    private long codeCount;


    public FileCount(String language, long blankCount, long commentsCount, long codeCount) {
        this.language = language;
        this.blankCount = blankCount;
        this.commentsCount = commentsCount;
        this.codeCount = codeCount;
    }


    public FileCount(FileCount fileCount) {
        this.language = fileCount.getLanguage();
        this.blankCount = fileCount.getBlankCount();
        this.commentsCount = fileCount.getCommentsCount();
        this.codeCount = fileCount.getCodeCount();
    }

    public String getLanguage() {
        return language;
    }

    public long getBlankCount() {
        return blankCount;
    }


    public long getCommentsCount() {
        return commentsCount;
    }


    public long getCodeCount() {
        return codeCount;
    }

    public void blankCountIncrement() {
        this.blankCount += 1;
    }

    public void commentCountIncrement() {
        this.commentsCount += 1;
    }

    public void codeCountIncrement() {
        this.codeCount += 1;
    }

    public void add(FileCount fileCount) {
        this.blankCount += fileCount.getBlankCount();
        this.commentsCount += fileCount.getCommentsCount();
        this.codeCount += fileCount.getCodeCount();
    }
}
