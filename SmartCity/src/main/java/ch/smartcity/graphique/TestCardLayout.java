package ch.smartcity.graphique;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestCardLayout {

    private int index = 0;

    public TestCardLayout() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                final CardLayout cl = new CardLayout();
                final JPanel cardPane = new JPanel(cl);
                cardPane.add(new JLabel("Hello"), "1");
                cardPane.add(new JScrollPane(createForm()), "2");

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(cardPane);
                frame.setSize(200, 200);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                JButton next = new JButton("Next");
                next.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        index++;
                        if (index > 1) {
                            index = 0;
                        }
                        if (index == 0) {
                            cl.show(cardPane, "1");
                        } else {
                            cl.show(cardPane, "2");
                        }
                    }
                });
                frame.add(next, BorderLayout.SOUTH);
            }

        });
    }

    public static void main(String[] args) {
        new TestCardLayout();
    }

    public JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        for (int index = 0; index < 100; index++) {
            form.add(new JTextField(10), gbc);
        }
        return form;
    }
}