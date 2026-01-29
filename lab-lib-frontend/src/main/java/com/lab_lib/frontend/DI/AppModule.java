package com.lab_lib.frontend.DI;

import com.google.inject.AbstractModule;
import com.lab_lib.frontend.Interfaces.IBookService;
import com.lab_lib.frontend.Interfaces.IPersonalLibraryService;
import com.lab_lib.frontend.Interfaces.IRatingService;
import com.lab_lib.frontend.Interfaces.IAuthService;
import com.lab_lib.frontend.Services.BookService;
import com.lab_lib.frontend.Services.AuthService;
import com.lab_lib.frontend.Services.PersonalLibraryService;
import com.lab_lib.frontend.Services.RatingService;
import com.lab_lib.frontend.Utils.HttpUtil;
import com.lab_lib.frontend.Utils.UserSession;
import com.google.inject.Singleton;

public class AppModule extends AbstractModule {

    public AppModule() {
       
    }

    @Override
    protected void configure() {
        // Bind implementations to interfaces
        bind(IBookService.class).to(BookService.class);
        bind(IAuthService.class).to(AuthService.class);
        bind(IPersonalLibraryService.class).to(PersonalLibraryService.class);
        bind(IRatingService.class).to(RatingService.class);
        
        // Bind concrete instances
        // HttpUtil verrà costruito da Guice con UserSession iniettata
        bind(HttpUtil.class);
        bind(UserSession.class).in(Singleton.class);
    }
}
