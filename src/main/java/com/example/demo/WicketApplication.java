package com.example.demo;


import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import com.example.demo.controllers.HomePage;

public class WicketApplication extends WebApplication {

	@Override
    public Class<? extends WebPage> getHomePage() {
		System.out.println("HOLASDF");
        return HomePage.class;
    }
	
	
	@Override
    public void init() {
		super.getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }
	
	

}