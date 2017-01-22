package simplenote.util;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;
import org.nutz.dao.util.Daos;
import simplenote.entity.Group;
import simplenote.entity.Note;

/**
 *
 * @author dhc-user
 */
public class DbManager {
    private final Dao dao;
    private static final DbManager instance = new DbManager();
    private DbManager(){
        SimpleDataSource sds = new SimpleDataSource();
        sds.setJdbcUrl("jdbc:h2:./note");
        dao = new NutDao(sds);
        dao.create(Group.class, false);
        Daos.migration(dao, Group.class, true, true, false);
        dao.create(Note.class, false);
        Daos.migration(dao, Note.class, true, true, false);
    }
    
    public static DbManager me(){
        return instance;
    }
    
    public static Dao dao(){
        return instance.getDao();
    }
    
    public Dao getDao(){
        return dao;
    }
}
