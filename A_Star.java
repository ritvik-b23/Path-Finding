// MARK: Notes:
/* There are 3 main things in the algorithm of A*:
 * 1) G-cost
 *      - it is the distance between the CURRENT and the START nodes
 * 2) H-cost
 *      - it is the distance between the CURRENT and the END nodes
 * 3) f-cost
 *      - it is the sum of G-cost and H-cost
 *      - this is the most important of all 3
 */


// MARK: Imports
import java.awt.Color;
import java.awt.Dimension;
// import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

// MARK: Class: DemoPanel
class DemoPanel extends JPanel {

    final int maxCol = 80;
    final int maxRow = 50;
    final int nodeSize = 70;
    // final int screenWidth = nodeSize * maxCol;
    // final int screeHeight = nodeSize * maxRow;
    final int screenWidth = 1900;
    final int screeHeight = 1500;

    // SCREEN SETTINGS
    Node [][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    // others
    boolean goalReached = false;
    int step = 0;
    


    public DemoPanel() {
        // this.setSize(screenWidth,screeHeight);
        // this.setPreferredSize(new Dimension(screenWidth, screeHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);
        


        // PLACE NODES
        int col = 0;
        int row = 0;

        while(col < maxCol && row < maxRow) {
            node[col][row] = new Node(col, row);
            this.add(node[col][row]);

            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }

        // SET START AND GOAL NODES
        setStartNode(0, 0);
        // setGoalNode(12, 6);
        setGoalNode(76, 45);

        // PLACE SOLID NODES

        setSolidNode(30, 0);
        setSolidNode(0, 30);
        Random rand = new Random();
        for (int i = 0; i < 2000; i++) {
            int randomSolidNodeX = rand.nextInt((78-1) + 1) + 1;
            int randomSolidNodeY = rand.nextInt((48-1) + 1) + 1;
            if (node[randomSolidNodeX][randomSolidNodeY] != startNode && node[randomSolidNodeX][randomSolidNodeY] != goalNode)
                setSolidNode(randomSolidNodeX, randomSolidNodeY);
        }


        // SET COST
        setCostOnNodes();

    }

    private void setStartNode(int col, int row) {
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }
    
    private void setGoalNode(int col, int row) {
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    private void setSolidNode(int col, int row) {
        node[col][row].setAsSolid();
    }

    private void setCostOnNodes() {
        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow) {
            getCost(node[col][row]);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {

        // Getting the G-cost 
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;
        
        // Getting the H-cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // Getting the F-Cost
        node.fCost = node.gCost + node.hCost;

        // Display the cost on node
        // if (node != startNode && node != goalNode) {
        //     node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        // } 
    }

    public void search() {
        if (goalReached == false && step < 300) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Open the UP node
            if (row-1 >= 0)
                openNode(node[col][row-1]);
            
            // Open the LEFT node
            if (col-1 >= 0)
                openNode(node[col-1][row]);
            
            // Open the DOWN node
            if (row+1 < maxRow)
                openNode(node[col][row+1]);
            
            // Open the RIGHT node
            if (col+1 < maxCol)
                openNode(node[col+1][row]);

            
            //FIND THE BEST NODE
            int bestNodeIndex = 0;
            int bestNodeFcost = 999;

            // after this loop is done we get the next best node
            for (int i = 0; i < openList.size(); i++) {

                // check if this node's F-cost is better
                if (openList.get(i).fCost < bestNodeFcost) {
                    bestNodeIndex = i;
                    bestNodeFcost = openList.get(i).fCost;
                }

                // if F-cost is equal then check G-cost
                else if (openList.get(i).fCost == bestNodeFcost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
            }
        }
        step++;
    }

    public void autoSearch() {
        while (goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Open the UP node
            if (row-1 >= 0)
                openNode(node[col][row-1]);
            
            // Open the LEFT node
            if (col-1 >= 0)
                openNode(node[col-1][row]);
            
            // Open the DOWN node
            if (row+1 < maxRow)
                openNode(node[col][row+1]);
            
            // Open the RIGHT node
            if (col+1 < maxCol)
                openNode(node[col+1][row]);

            
            //FIND THE BEST NODE
            int bestNodeIndex = 0;
            int bestNodeFcost = 999;

            // after this loop is done we get the next best node
            for (int i = 0; i < openList.size(); i++) {

                // check if this node's F-cost is better
                if (openList.get(i).fCost < bestNodeFcost) {
                    bestNodeIndex = i;
                    bestNodeFcost = openList.get(i).fCost;
                }

                // if F-cost is equal then check G-cost
                else if (openList.get(i).fCost == bestNodeFcost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }
        }
    }


    private void openNode(Node node) {
        if (node.open == false && node.checked == false && node.solid == false) {

            // if node is not opened yet, add it to the open list
            node.setAsOpen();
            node.parent = currentNode; // we need the parent to track back the path after reaching the goal
            openList.add(node);
        }
    }

    private void trackPath() { // we call this method when we reach the goal (line: 263)

        // we backtrack and draw the best path
        Node current = goalNode;

        while (current != startNode) {
            current = current.parent;

            if (current != startNode) {
                current.setAsPath();
            }
        }
    }
}

// MARK: Class Node
class Node extends JButton implements ActionListener {

    Node parent;
    int col, row, gCost, hCost, fCost;
    boolean start, goal, solid, open, checked;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;

        this.setPreferredSize(new Dimension(20,20));
        setBackground(Color.white);
        setForeground(Color.black);
        addActionListener(this);
    }

    public void setAsStart() {
        setBackground(Color.blue);
        setForeground(Color.white);
        // setText("Start");
        start = true;
    }

    public void setAsGoal() {
        setBackground(Color.red);
        setForeground(Color.black);
        // setText("Goal");
        goal = true;
    }

    public void setAsSolid() {
        setBackground(Color.black);
        setForeground(Color.black);
        solid = true;
    }

    public void setAsOpen() {
        open = true;

    }

    public void setAsChecked() {
        if (start == false && goal == false) {
            setBackground(Color.yellow);
            setForeground(Color.black);
        }

        checked = true;
    }

    public void setAsPath() {
        setBackground(Color.magenta);
        setForeground(Color.black);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setBackground(Color.orange);
    }

}

// MARK: Class KeyHandler
class KeyHandler implements KeyListener {
    DemoPanel dp;
    public KeyHandler(DemoPanel dp) {
        this.dp = dp;
    }

    @Override
    public void keyTyped(KeyEvent e) {} // no use

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER) {
            // dp.search();
            dp.autoSearch();
        }

        else if (code == KeyEvent.VK_ESCAPE) { System.exit(0); }
    }

    @Override
    public void keyReleased(KeyEvent e) {} // no use
    
}


// MARK: Class: Main 
public class A_Star {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(new DemoPanel());

        // window.setSize(500,500);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
    }
}