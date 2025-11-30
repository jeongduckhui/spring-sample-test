package com.example.demo.domain.dynamic;

public enum MenuColumn {

	MENU_ID("menuId", "MENU_ID"),
	COMPONENT("component", "COMPONENT"),
	PATH("path", "PATH"),
	REDIRECT("redirect", "REDIRECT"),
	NAME("name", "name"),
	TITLE("title", "TITLE"),
	ICON("icon", "ICON"),
	PARENT_ID("parentId", "PARENT_ID"),
	IS_LEAF("isLeaf", "IS_LEAF"),
	HIDDEN("hidden", "HIDDEN");
	
	private final String paramName;
	private final String columnName;
	
	MenuColumn(String paramName, String columnName) {
		this.paramName = paramName;
		this.columnName = columnName;
	}
	
	public String getParamName() {
        return paramName;
    }
	
	public String getColumnName() {
        return columnName;
    }
}
