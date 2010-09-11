/*
 * AssignmentManager
 *
 * Copyright (C) 2010  Jaroslav Merxbauer
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package notwa.gui;

import notwa.gui.datamodels.KeyValueListModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import notwa.common.ConnectionInfo;
import notwa.logger.LoggingFacade;
import notwa.dal.ProjectDal;
import notwa.dal.ProjectToUserAssignmentDal;
import notwa.dal.UserDal;
import notwa.gui.components.KeyValueComboBox;
import notwa.gui.components.KeyValueList;
import notwa.wom.Context;
import notwa.wom.Project;
import notwa.wom.ProjectCollection;
import notwa.wom.User;
import notwa.wom.UserCollection;

public class AssignmentManager extends JDialog implements ActionListener, ListSelectionListener {
    private JButton okButton, stornoButton, addButton, removeButton;
    private KeyValueComboBox<Project> projects;
    private KeyValueList<User> users;
    private KeyValueList<User> currentlyAssignedUsers;
    private Context context;
    private ConnectionInfo ci;
    private KeyValueListModel<User> projectUsersModel = new KeyValueListModel<User>();
    private KeyValueListModel<User> usersModel = new KeyValueListModel<User>();
    private ArrayList<User> assignedUsers = new ArrayList<User>(); //used only for checking if user is already assigned to project
    private Project currentlySelectedProject;
    
    public AssignmentManager(ConnectionInfo ci, Context context) {
        this.context = context;
        this.ci = ci;
        init();
    }
    
    public void init() {
        this.setLayout(new BorderLayout());
        this.setTitle("NOTWA - NOT Only Team Work Assistent - Assignment Manager");
        this.setSize(500,300);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setLocationRelativeTo(null);
        
        this.add(this.initMainComponents(),BorderLayout.CENTER);
        this.add(this.initButtons(), BorderLayout.PAGE_END);
        
        this.setVisible(true);
    }
    
    public void setSelection(Project project) {
        projects.setSelectedItem(project.getName()); // TODO
    }
    
    private JPanel initMainComponents() {
        JPanel componentsPanel = new JPanel();
        componentsPanel.setLayout(null);

        JLabel lProject = new JLabel("Project");
        componentsPanel.add(lProject);
        lProject.setBounds(149, 12, 61, 22);
        componentsPanel.add(getAllProjects());

        JLabel lAllRegisteredUsers = new JLabel("All registered users");
        componentsPanel.add(lAllRegisteredUsers);
        lAllRegisteredUsers.setBounds(60, 56, 135, 15);

        users = new KeyValueList<User>(usersModel);
        users.addListSelectionListener(this);
        JScrollPane allUsersPanel = new JScrollPane(users);
        allUsersPanel.setBounds(62, 77, 115, 130);
        componentsPanel.add(allUsersPanel);
        
        addButton = new JButton("Add >");
        addButton.addActionListener(this);
        removeButton = new JButton("< Remove");
        removeButton.addActionListener(this);
        componentsPanel.add(addButton);
        addButton.setBounds(192, 99, 112, 20);
        componentsPanel.add(removeButton);
        removeButton.setBounds(192, 124, 112, 20);
        addButton.setEnabled(false);
        removeButton.setEnabled(false);

        JLabel lAssignedUsers = new JLabel("Already assigned users");
        componentsPanel.add(lAssignedUsers);
        lAssignedUsers.setBounds(305, 52, 152, 22);
        currentlyAssignedUsers = new KeyValueList<User>(projectUsersModel);
        currentlyAssignedUsers.addListSelectionListener(this);
        JScrollPane assignedUsersPanel = new JScrollPane(currentlyAssignedUsers);
        assignedUsersPanel.setBounds(320, 77, 115, 130);
        componentsPanel.add(assignedUsersPanel);
        
        return componentsPanel;
    }
    
    private KeyValueComboBox<Project> getAllProjects() {
        projects = new KeyValueComboBox<Project>();
        projects.setBounds(210, 14, 144, 18);
        projects.addActionListener(this);
        
        ProjectCollection pc = new ProjectCollection();
        pc.setCurrentContext(context);
        ProjectDal pDal = new ProjectDal(ci, pc.getCurrentContext());
        pDal.fill(pc);
        
        for (Project project : pc) {
            projects.addItem(project, project.getName());
        }
        
        return projects;
    }
    
    private void getAllUsers() {
        try {
            usersModel.clear(); // clear existing items in list
        }
        catch (Exception e) { }
        
        UserCollection uc = new UserCollection();
        uc.setCurrentContext(context);
        UserDal uDal = new UserDal(ci, uc.getCurrentContext());
        uDal.fill(uc);
        
        for (User user : uc) {
            if (!assignedUsers.contains(user)) {
                usersModel.addElement(user, user.getLogin());
            }
        }
    }
    
    private void getAllProjectUsers() {
        try {
            projectUsersModel.clear(); // clear existing items in list
        }
        catch (Exception e) { }
        try {
            assignedUsers.clear();
        }
        catch (Exception e) { }
        
        UserCollection usersInProject = (projects.getSelectedKey()).getAssignedUsers();
        
        for (User user : usersInProject) {
            projectUsersModel.addElement(user, user.getLogin());
            assignedUsers.add(user);
        }
    }
    
    private JPanel initButtons() {
        JPanel buttonsPanel = new JPanel();
        
        okButton = new JButton("Ok");
        stornoButton = new JButton("Storno");
        
        okButton.addActionListener(this);
        stornoButton.addActionListener(this);
        
        buttonsPanel.add(okButton);
        buttonsPanel.add(stornoButton);
        
        return buttonsPanel;
    }

    private void save() {
        if (    !assignedUsers.containsAll(projectUsersModel.toCollection()) ||
                !projectUsersModel.toCollection().containsAll(assignedUsers)) { // check if something has changed
            if (JOptionPane.showConfirmDialog(this, "Do you really want to save changes?") == 0) {
                ProjectToUserAssignmentDal ptuad = new ProjectToUserAssignmentDal(ci, context);
                ptuad.update(currentlySelectedProject.getAssignedUsers());
            }
        }        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == okButton) {
            this.save();
            this.setVisible(false);
        }
        
        if (ae.getSource() == stornoButton) {
            this.setVisible(false);
        }
        
        if (ae.getSource() == projects) {
            this.save();
            
            this.getAllProjectUsers();
            try { currentlyAssignedUsers.updateUI(); } catch (Exception e) { }
            this.getAllUsers();
            try { users.updateUI(); } catch (Exception e) { }
            try { addButton.setEnabled(false); } catch (Exception e) { }
            try { removeButton.setEnabled(false); } catch (Exception e) { }
        }
        
        if (ae.getSource() == addButton) {
            currentlySelectedProject = projects.getSelectedKey();
            if (users.getSelectedValues().length != 0) {
                for (User user : users.getSelectedKeys()) {
                    try {
                        user.setInserted(true);
                        currentlySelectedProject.addAssignedUser(user);
                    } catch (Exception e) {
                        LoggingFacade.handleException(e);
                    }
                    projectUsersModel.addElement(user, user.getLogin());
                    usersModel.removeKey(user);
                }
            }
        }
        
        if (ae.getSource() == removeButton) {
            currentlySelectedProject = projects.getSelectedKey();
            if (currentlyAssignedUsers.getSelectedValues().length != 0) {
                for (User user : currentlyAssignedUsers.getSelectedKeys()) {
                    try {
                        user.setDeleted(true);
                    } catch (Exception e) {
                        LoggingFacade.handleException(e);
                    }
                    usersModel.addElement(user, user.getLogin());
                    projectUsersModel.removeKey(user);
                }
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getSource() == users) {
            if (users.getSelectedValues().length > 0) {
                currentlyAssignedUsers.clearSelection();
                addButton.setEnabled(true);
            }
            else {
                addButton.setEnabled(false);
            }
        }
        
        if (lse.getSource() == currentlyAssignedUsers) {
            if (currentlyAssignedUsers.getSelectedValues().length > 0) {
                users.clearSelection();
                removeButton.setEnabled(true);                
            }
            else {
                removeButton.setEnabled(false);
            }
        }
    }
}
