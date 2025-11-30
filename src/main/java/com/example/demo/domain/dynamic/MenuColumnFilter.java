package com.example.demo.domain.dynamic;

import lombok.Data;

@Data
public class MenuColumnFilter {
	
	// MyBatis XML의 test="필드명 == true" 와 매핑되는 boolean 필드
	private boolean MENU_ID;
	private boolean COMPONENT;
	private boolean PATH;
	private boolean REDIRECT;
	private boolean NAME;
	private boolean TITLE;
	private boolean ICON;
	private boolean PARENT_ID;
	private boolean IS_LEAF;
	private boolean HIDDEN;

}
