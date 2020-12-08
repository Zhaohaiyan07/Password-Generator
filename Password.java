package com.common;

public class Password {
    private boolean star;
    private String text;

    public Password() {
    }

    public Password(String text) {
        this.text = text;
    }

    public Password(boolean star, String text) {
        this.star = star;
        this.text = text;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
