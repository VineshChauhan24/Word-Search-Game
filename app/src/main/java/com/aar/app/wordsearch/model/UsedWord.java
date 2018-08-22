package com.aar.app.wordsearch.model;

/**
 * Created by abdularis on 08/07/17.
 */

public class UsedWord extends Word {

    private AnswerLine mAnswerLine;
    private boolean mAnswered;
    private boolean mIsMystery;
    private int mRevealCount;

    public UsedWord() {
        mAnswerLine = null;
        mAnswered = false;
        mIsMystery = false;
        mRevealCount = 0;
    }

    public AnswerLine getAnswerLine() {
        return mAnswerLine;
    }

    public void setAnswerLine(AnswerLine answerLine) {
        mAnswerLine = answerLine;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }

    public boolean isMystery() {
        return mIsMystery;
    }

    public void setIsMystery(boolean isMystery) {
        mIsMystery = isMystery;
    }

    public int getRevealCount() {
        return mRevealCount;
    }

    public void setRevealCount(int revealCount) {
        mRevealCount = revealCount;
    }

    public static final class AnswerLine {
        public int startRow;
        public int startCol;
        public int endRow;
        public int endCol;
        public int color;

        public AnswerLine() {
            this(0, 0, 0, 0, 0);
        }

        public AnswerLine(int startRow, int startCol, int endRow, int endCol, int color) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
            this.color = color;
        }

        @Override
        public String toString() {
            return startRow + "," + startCol + ":" + endRow + "," + endCol;
        }

        public void fromString(String string) {
            /*
                Expected format string = 1,1:6,6
             */
            if (string == null) return;

            String split[] = string.split(":", 2);
            if (split.length >= 2) {
                String start[] = split[0].split(",", 2);
                String end[] = split[1].split(",", 2);

                if (start.length >= 2 && end.length >= 2) {
                    startRow = Integer.parseInt(start[0]);
                    startCol = Integer.parseInt(start[1]);
                    endRow = Integer.parseInt(end[0]);
                    endCol = Integer.parseInt(end[1]);
                }
            }
        }

    }

}
