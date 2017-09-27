package com.l2jfrozen.netcore.deamon.data;

import a.a.g;
import a.a.i;

public class MarshallerUnmarshallerTest {

	public static void main(String[] args) {
		
		i serverConfigInfoIn = new i();
		String content = g.g().a(serverConfigInfoIn);
		System.out.println(content);
		a.a.i serverConfigInfo = (i) g.g().f(content);
		System.out.println(serverConfigInfo.ggsi());
		
	}

}
