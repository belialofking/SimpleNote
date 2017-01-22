package simplenote.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import simplenote.dao.NoteDao;
import simplenote.entity.Note;

public class MyTabSheet extends JPanel {

    private final JScrollPane jSPane = new JScrollPane();
    private final JTextPane jTextPane1 = new JTextPane();
    private final JTextField jTextTitle = new JTextField();
    private final Note note;
    private final JTabbedPane jTabbedPane;

    public MyTabSheet(Note note, JTabbedPane jTabbedPane) {
        this.note = note;
        this.jTabbedPane = jTabbedPane;
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.add(jSPane, BorderLayout.CENTER);
        this.add(jTextTitle, BorderLayout.NORTH);
        
        jTextTitle.setText(note.getTitle());
        jTextPane1.setText(note.getContent());
        jSPane.setViewportView(jTextPane1);
        jTextPane1.setFont(new java.awt.Font("宋体", 0, 14));
    }

    public void save() {
        note.setContent(this.jTextPane1.getText());
        note.setTitle(this.jTextTitle.getText());
        int index = jTabbedPane.indexOfComponent(this);
        jTabbedPane.setTitleAt(index, note.getTitle());
        NoteDao.update(note);
    }

    public Note getNote() {
        return note;
    }

    public void remove() {
        NoteDao.delete(this.note.getNoteId());
        jTabbedPane.remove(this);
    }
}
