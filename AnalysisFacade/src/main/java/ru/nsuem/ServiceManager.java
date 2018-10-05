package ru.nsuem;

import ru.nsuem.sql.SQLHelper;
import ru.nsuem.view.ViewManager;

import java.io.InputStream;
import java.net.URL;

public class ServiceManager {
    private static ServiceManager INSTANCE=new ServiceManager();
    private static SQLHelper sqlHelper;
    private static ViewManager viewManager;

    private ServiceManager() {
        try {
            sqlHelper = new SQLHelper();
            URL o=this.getClass().getResource("/view.properties.json");
            InputStream stream = o.openStream();
            viewManager=new ViewManager(stream);
        } catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static ServiceManager getINSTANCE() {
        return INSTANCE;
    }

    public SQLHelper getSqlHelper() {
        return sqlHelper;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }
}
