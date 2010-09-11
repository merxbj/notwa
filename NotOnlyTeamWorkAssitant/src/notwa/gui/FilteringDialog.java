/*
 * FilteringDialog
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

import javax.swing.JDialog;

public class FilteringDialog extends JDialog {

    public FilteringDialog() {
        init();
    }
    
    public void init() {
        this.setTitle("NOTWA - NOT Only Team Work Assistent - Configure Sorting / Filtering");
        this.setSize(500,200);
        this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
                
       
        this.setVisible(true);
    }
}
