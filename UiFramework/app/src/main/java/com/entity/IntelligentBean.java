package com.entity;

import java.util.List;

/**
 * Created by vicmob_yf002 on 2017/6/24.
 * 智能回复实体对象
 */
public class IntelligentBean {

    private List<VicCodetextBean> vic_codetext;

    public List<VicCodetextBean> getVic_codetext() {
        return vic_codetext;
    }

    public void setVic_codetext(List<VicCodetextBean> vic_codetext) {
        this.vic_codetext = vic_codetext;
    }

    public static class VicCodetextBean {
        /**
         * keyword
         * text
         */

        private String keyword;
        private String text;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
