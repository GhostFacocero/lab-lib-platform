package com.lab_lib.frontend.DI;

import com.google.inject.AbstractModule;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Services.BookService;
import com.lab_lib.frontend.Utils.HttpUtil;

public class AppModule extends AbstractModule {

    public AppModule() {
       
    }

    @Override
    protected void configure() {
        // Bind implementations to interfaces
        bind(IBookService.class).to(BookService.class);
        
        // Bind concrete instances
        bind(HttpUtil.class).toInstance(new HttpUtil());
    }
}
