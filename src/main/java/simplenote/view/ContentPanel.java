package simplenote.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.nutz.lang.Strings;
import simplenote.dao.NoteDao;
import simplenote.entity.Note;

public class ContentPanel extends JPanel {

    private String groupId;

    public String getGroupId() {
        return groupId;
    }


    private final JTabbedPane jTabbedPane = new JTabbedPane();

    public ContentPanel() {
        init();
    }

    private void init() {
        BorderLayout blr = new BorderLayout();
        setLayout(blr);
        JPanel rightBtnPanel = genereateBtnPanel();
        add(rightBtnPanel, BorderLayout.NORTH);
        add(jTabbedPane, BorderLayout.CENTER);
    }

    private JPanel genereateBtnPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton newNote = new JButton("新增");
        newNote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Strings.isBlank(groupId)) {
                    return;
                }
                Note note = NoteDao.create("note1", "", groupId);
                MyTabSheet sheet = createTab(note);
                jTabbedPane.setSelectedComponent(sheet);
            }
        });
        panel.add(newNote);
        JButton saveNote = new JButton("保存");
        saveNote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = jTabbedPane.getSelectedIndex();
                if (index < 0) {
                    return;
                }
                MyTabSheet sheet = (MyTabSheet) jTabbedPane.getComponentAt(index);
                sheet.save();
            }
        });
        panel.add(saveNote);
        JButton deleteNote = new JButton("删除");
        deleteNote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = jTabbedPane.getSelectedIndex();
                if (index < 0) {
                    return;
                }
                MyTabSheet sheet = (MyTabSheet) jTabbedPane.getComponentAt(index);
                sheet.remove();
            }
        });
        panel.add(deleteNote);
        return panel;
    }

    public MyTabSheet createTab(Note note) {
        MyTabSheet sheet = new MyTabSheet(note, jTabbedPane);
        jTabbedPane.addTab(note.getTitle(), sheet);
        return sheet;

    }

    public void showTab(String groupId) {
        clearTab();
        this.groupId = groupId;
        List<Note> notes = NoteDao.queryListByGroup(groupId);
        for (Note note : notes) {
            createTab(note);
        }
    }

    public void clearTab() {
        this.groupId = "";
        jTabbedPane.removeAll();
    }

    public static void main(String[] args) {
        ContentPanel panel = new ContentPanel();
        JFrame f = new JFrame("JTreeDemo");
        f.add(panel);
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        panel.showTab("13f53105a607421baaf7f6d4f7ed4e80");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
