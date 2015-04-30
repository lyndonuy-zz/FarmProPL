/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lyndon
 */
import java.awt.*;
import java.awt.geom.*;
public class GenPath_Logo_UY extends Frame{
    public void paint(Graphics g){
        Graphics2D g2d =(Graphics2D) g;
        BasicStroke bs = new BasicStroke(10.0f);
        g2d.setStroke(bs);
        g2d.setPaint(Color.green);
        GeneralPath gp = new GeneralPath();
        gp.moveTo(70,200);
        gp.lineTo(1,320);
        gp.lineTo(140,320);
        gp.lineTo(70,200);
        g2d.draw(gp);
        g2d.setPaint(Color.red);
        Ellipse2D.Double el = new Ellipse2D.Double(140,200,135,129);
        g2d.draw(el);
        gp = new GeneralPath();
        
        g2d.setPaint(Color.blue);
        gp.moveTo(285,200);
        gp.lineTo(395,315);
        g2d.draw(gp);
        gp = new GeneralPath();
        
        g2d.setPaint(Color.blue);
        gp.moveTo(395,200);
        gp.lineTo(285,315);
        g2d.draw(gp);
        Rectangle2D.Double rekt = new Rectangle2D.Double(420,200,125,125);
        g2d.draw(rekt);
        g2d.setPaint(Color.black);
        bs = new BasicStroke (2.0f);
        g2d.setStroke(bs);
        Font font = new Font("Arial", Font.BOLD, 36);
        g2d.setFont(font);
        g2d.drawString("Live In Y",50,380);
        g2d.setStroke(bs);
        g2d.setPaint(Color.red);
        el = new Ellipse2D.Double(190,360,20,18);
        g2d.draw(el);
        g2d.setPaint(Color.black);
        g2d.drawString("ur, W", 210, 380);
        g2d.setPaint(Color.blue);
        g2d.drawString("x",300,380);
        g2d.setPaint(Color.black);
        g2d.drawString("rld, Pl", 320, 380);
        /*g2d.setPaint(Color.green);
        Line2D.Double F = new Line2D.Double(440,365,430,380);
        g2d.draw(F);
        Line2D.Double G = new Line2D.Double(430,380,445,380);
        g2d.draw(G);
        Line2D.Double H = new Line2D.Double(445,380,440,365);
        g2d.draw(H);*/
        g2d.setPaint(Color.green);
        gp = new GeneralPath();
        gp.moveTo(440,365);
        gp.lineTo(430,380);
        gp.lineTo(445,380);
        gp.lineTo(440,365);
        g2d.draw(gp);
        g2d.setPaint(Color.black);
        g2d.drawString("y In ", 450, 380);
        g2d.setPaint(Color.pink);
        rekt = new Rectangle2D.Double(520, 360, 20,20);
        g2d.draw(rekt);
        g2d.setPaint(Color.black);
        g2d.drawString("urs.", 550, 380);
        el = new Ellipse2D.Double(555,280,50,50);
        g2d.draw(el);
        g2d.drawString("R", 565, 320);
    }
    public static void main(String[] args) {
        GenPath_Logo_UY s = new GenPath_Logo_UY();
        s.setTitle("STRING IN JAVA 2D");
        s.setBackground(Color.white);
        s.setSize(800,800);
        s.setVisible(true);
        s.setForeground(Color.black);
        //System.exit(0);
    }
}
