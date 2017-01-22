package simplenote.dao;

import java.util.ArrayList;
import java.util.List;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import simplenote.util.DbManager;
import simplenote.util.IDUtil;
import simplenote.entity.Note;

public class NoteDao {
    public static Note create(String title ,String content,String groupId) {
        Note note = new Note();
        if(Strings.isBlank(title)){
            return null;
        }
        if(Strings.isBlank(groupId)){
            return null;
        }
        note.setNoteId(IDUtil.generateId());
        note.setTitle(title);
        note.setContent(content);
        note.setGroupId(groupId);
        note.setCreateAt(System.currentTimeMillis());
        return DbManager.dao().insert(note);
    }
    public static Note update(Note note){
        DbManager.dao().update(note);
        return note;
    }
    public static List<Note> queryListByGroup(String groupId){
        if(Strings.isBlank(groupId)){
            return new ArrayList<Note>();
        }
        List groups = DbManager.dao().query(Note.class, Cnd.where("groupId", "=", groupId).orderBy("createAt","asc"));
        return groups;
    }
    public static void delete(String noteId){
        DbManager.dao().delete(Note.class,noteId);
    }
}
