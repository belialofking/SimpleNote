package simplenote.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.nutz.lang.Strings;
import simplenote.listener.GroupChangeListener;
import simplenote.dao.GroupDao;
import simplenote.entity.Group;

public class NavPanel extends JPanel {

    private JTree tree;
    private DefaultMutableTreeNode curNode;
    private GroupChangeListener groupChangeListener;

    public GroupChangeListener getGroupChangeListener() {
        return groupChangeListener;
    }

    public void setGroupChangeListener(GroupChangeListener groupChangeListener) {
        this.groupChangeListener = groupChangeListener;
    }

    public NavPanel() {
        init();
    }

    private void init() {
        BorderLayout bll = new BorderLayout();
        this.setLayout(bll);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("分类");
        initTree(Group.ROOT, top);
        curNode = top;
        tree = new JTree(top);
        selectedNode(top);
        this.add(tree, BorderLayout.CENTER);
        JPanel leftBtnPanel = new JPanel();

        JButton newCate = new JButton("新增");
        newCate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String msg = JOptionPane.showInputDialog("请输入分类名称!");
                if (msg == null || msg.isEmpty()) {
                    return;
                }
                String parentId = getSelectedNode();
                if (Strings.isBlank(parentId)) {
                    parentId = Group.ROOT;
                }
                Group group = GroupDao.create(msg, parentId);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(group);
                curNode.add(node);
                tree.repaint();
                tree.updateUI();
                selectedNode(node);
            }
        });

        leftBtnPanel.add(newCate);
        JButton delNode = new JButton("删除");
        delNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String parentId = getSelectedNode();
                if (Strings.isBlank(parentId)) {
                    return;
                }
                if (!GroupDao.delete(parentId)) {
                    return;
                }

                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.removeNodeFromParent(curNode);
                tree.repaint();
                tree.updateUI();
            }
        });
        leftBtnPanel.add(delNode);
        JButton editNode = new JButton("修改");
        editNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String msg = JOptionPane.showInputDialog("请输入分类名称!");
                if (msg == null || msg.isEmpty()) {
                    return;
                }
                Object object = curNode.getUserObject();
                if (!(object instanceof Group)) {
                    return;
                }
                Group nd = (Group) object;
                nd.setLabel(msg);
                Group group = GroupDao.update(nd);
                curNode.setUserObject(group);

                tree.repaint();
                tree.updateUI();
            }
        });
        leftBtnPanel.add(editNode);
        this.add(leftBtnPanel, BorderLayout.NORTH);
        // 添加选择事件
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                String groupId = getSelectedNode();
                if (groupChangeListener != null) {
                    groupChangeListener.change(groupId);
                }
            }

        });
    }

    private void initTree(String parentId, DefaultMutableTreeNode parentNode) {
        List<Group> groups = GroupDao.queryListByParent(parentId);
        for (Group group : groups) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(group);
            parentNode.add(node);
            initTree(group.getId(), node);
        }

    }

    private String getSelectedNode() {
        curNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (curNode == null) {
            return "";
        }

        Object object = curNode.getUserObject();
        if (!(object instanceof Group)) {
            return "";
        }
        Group nd = (Group) object;
        return nd.getId();
    }

    private void selectedNode(DefaultMutableTreeNode top) {
        TreePath treePath = new TreePath(top.getPath());
        tree.setSelectionPath(treePath);
    }

}
