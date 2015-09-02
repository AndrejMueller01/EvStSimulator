package es.ui;

import es.StrategyMuRhoLambda;
import es.StrategyOnePlusOne;
import es.Strategy;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Johannes on 31/08/2015.
 */
public class StrategyForm extends JFrame {
    public JTextField targetStringTextField;
    public JTextField iterationsTextField;
    public JComboBox strategyComboBox;
    public JButton runButton;
    public JTextField populationSizeTextField;
    public JPanel rootPanel;
    public JTextArea resultList;
	private JScrollPane scrollPane;
	public ArrayList<Strategy> strategies;


    public StrategyForm() {
        super("Evolution Strategy");
        pack();
        setContentPane(rootPanel);
        setSize(1024, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Strategy onePlusOne = new StrategyOnePlusOne(this);
        Strategy muPlusLambda = new StrategyMuRhoLambda(this);

        strategies = new ArrayList<Strategy>();
        strategies.add(onePlusOne);
        strategies.add(muPlusLambda);

        for (Strategy strategy : strategies) {
            strategyComboBox.addItem(strategy.getName());
        }
        getSelectedStrategy().adjustGui();

        strategyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(strategyComboBox.getSelectedItem().toString());
                getSelectedStrategy().adjustGui();
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultList.setText("");
                getSelectedStrategy().setTargetString(targetStringTextField.getText());
                getSelectedStrategy().setIterations(Integer.parseInt(iterationsTextField.getText()));
                getSelectedStrategy().setPopulationSize(Integer.parseInt(populationSizeTextField.getText()));
                getSelectedStrategy().setLambda(Integer.parseInt(populationSizeTextField.getText()) * 10);

                JOptionPane.showMessageDialog(null, getSelectedStrategy().evolution());
            }
        });

		KeyAdapter kA = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					runButton.doClick();
				}
			}
		};

		iterationsTextField.addKeyListener(kA);
		populationSizeTextField.addKeyListener(kA);
		targetStringTextField.addKeyListener(kA);
	}

    public Strategy getSelectedStrategy() {
        for (Strategy strategy : strategies) {
            if (strategy.getName().equals(strategyComboBox.getSelectedItem().toString())) {
                return strategy;
            }
        }
        return null;
    }
}
