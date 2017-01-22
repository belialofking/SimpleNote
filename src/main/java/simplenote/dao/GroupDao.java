package simplenote.dao;


import java.util.List;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import simplenote.util.DbManager;
import simplenote.util.IDUtil;
import simplenote.entity.Group;

public class GroupDao {
    
    public static Group create(String title ,String parent) {
        Group group = new Group();
        if(Strings.isBlank(parent)){
            parent = Group.ROOT;
        }
        group.setId(IDUtil.generateId());
        group.setParentId(parent);
        group.setLabel(title);
        group.setCreateAt(System.currentTimeMillis());
        DbManager.dao().insert(group);
        return group;
    }
    public static List<Group> queryListByParent(String parent){
        if(Strings.isBlank(parent)){
            parent = Group.ROOT;
        }
        List groups = DbManager.dao().query(Group.class, Cnd.where("parentId", "=", parent).orderBy("createAt", "asc"));
        return groups;
    }
    
    public static boolean delete(String groupId){
        List<Group> list = queryListByParent(groupId);
        if(!list.isEmpty()){
            return false;
        }
        DbManager.dao().delete(Group.class, groupId);
        return true;
    }
    public static Group update(Group group){
        DbManager.dao().update(group);
        return group;
    }
}
