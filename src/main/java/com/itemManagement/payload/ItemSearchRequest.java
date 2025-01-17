package com.itemManagement.payload;

public class ItemSearchRequest {

    private String keyword;
    private String field;

    public ItemSearchRequest(){}

    public ItemSearchRequest(String keyword, String field) {
        this.keyword = keyword;
        this.field = field;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
