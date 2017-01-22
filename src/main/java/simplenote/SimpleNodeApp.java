package simplenote;

import simplenote.listener.GroupChangeListener;
import simplenote.view.ContentPanel;
import simplenote.view.NavPanel;
import javax.swing.*;
import org.nutz.lang.Strings;

public class SimpleNodeApp {

    private final ContentPanel contentPanel;

    public SimpleNodeApp() {
        contentPanel = new ContentPanel();
    }

    public void amain() {
        JSplitPane jSplitPane = new JSplitPane();
        NavPanel navPanel = new NavPanel();
        jSplitPane.setLeftComponent(navPanel);

        jSplitPane.setRightComponent(contentPanel);
        navPanel.setGroupChangeListener(new GroupChangeListener() {
            @Override
            public void change(String groupId) {
                if (Strings.isBlank(groupId)) {
                    contentPanel.clearTab();
                } else {
                    contentPanel.showTab(groupId);
                }
            }
        });
        JFrame f = new JFrame("简单笔记");
        f.add(jSplitPane);
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        //f.setExtendedState(Frame.MAXIMIZED_BOTH);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
        }
        SimpleNodeApp app = new SimpleNodeApp();
        app.amain();
    }

}
