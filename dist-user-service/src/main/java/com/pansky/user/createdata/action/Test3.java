package com.pansky.user.createdata.action;

import java.text.MessageFormat;

public class Test3 {
	public static void main(String[] args) {
		String table="Delivery_history";
		String raw=" insert into {0} select * from {1} ";
		Object[] arr={"HYBK_"+table,"HY_"+table,};
		String sql=MessageFormat.format(raw, arr);// MessageFormat 拼接字符串
		System.out.println(sql);
	}
}
