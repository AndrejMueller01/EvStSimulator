package es;

import es.ui.MainFrame;

/**
 *
 * @author Andrej
 */
public class Main {
    
      public static void main(String args[]) {
     
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
