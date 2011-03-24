/**
 * 
 */
package org.sikuli.guide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JTextPane;

import org.sikuli.script.Region;

public class SikuliGuideCallout extends SikuliGuideComponent{

   static final int TRIANGLE_SIZE = 20;
   static final int PADDING_X = 5;
   static final int PADDING_Y = 5;
   


   String text;

   public SikuliGuideCallout(SikuliGuide sikuliGuide, String text){         
      super(sikuliGuide);
      init(text);
   }

   HTMLTextPane textArea;
   RoundedBox rbox;
   Triangle triangle;

   class Triangle extends SikuliGuideComponent {

      GeneralPath gp;
      public Triangle(SikuliGuide guide) {
         super(guide);         
         gp = new GeneralPath();
         gp.moveTo(TRIANGLE_SIZE*0.45,0);
         gp.lineTo(TRIANGLE_SIZE*0.5,TRIANGLE_SIZE);
         gp.lineTo(TRIANGLE_SIZE*0.85,0);
         gp.closePath();

         setSize(new Dimension(TRIANGLE_SIZE,TRIANGLE_SIZE));
      }

      public void rotate(double radius){
         AffineTransform rat = new AffineTransform();
         rat.rotate(radius, TRIANGLE_SIZE/2, TRIANGLE_SIZE/2);
         gp.transform(rat);
      }

      public void paintComponent(Graphics g){
         super.paintComponent(g);
         Graphics2D g2d = (Graphics2D) g;

         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
               RenderingHints.VALUE_ANTIALIAS_ON); 
         g2d.fill(gp);
      }
   }

   class RoundedBox extends SikuliGuideComponent {

      public RoundedBox(SikuliGuide guide, Rectangle rect) {
         super(guide);

         setBounds(rect);
         setForeground(Color.yellow);
      }

      public void paintComponent(Graphics g){
         super.paintComponent(g);
         Graphics2D g2d = (Graphics2D) g;


         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
               RenderingHints.VALUE_ANTIALIAS_ON); 

         RoundRectangle2D roundedRectangle = new 
         RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
         g2d.fill(roundedRectangle);
      }

   }


   class HTMLTextPane extends JTextPane{
      static final int DEFAULT_MAXIMUM_WIDTH = 300;
      
      int maximum_width;
      String text;
      public HTMLTextPane(){
         maximum_width = DEFAULT_MAXIMUM_WIDTH;         
         setContentType("text/html");
         setBackground(Color.yellow);
      }
            
      @Override
      public void setText(String text){  
         this.text = text;
         String htmltxt = "<html>"+text+"</html>";
         
         super.setText(htmltxt);
         
         if (getPreferredSize().getWidth() > maximum_width){
            
            htmltxt = "<html><div width='"+maximum_width+"'>"+text+"</div></html>";
            super.setText(htmltxt);
         }
         setSize(getPreferredSize());
      }
      
      @Override
      public String getText(){
         return this.text;
      }
      
      void setMaximumWidth(int maximum_width){
         this.maximum_width = maximum_width;
         setText(this.text);
      }
   }
   
   void init(String text){
      this.text = text;

      //      JTextArea textArea = new JTextArea();
      //      //textArea.setColumns(20);
      //      textArea.setEditable(false);
      //      //textArea.setRows(5);
      //      textArea.setText(text);
      //      textArea.setLineWrap(true);
      //      textArea.setWrapStyleWord(true);
      //      

      textArea = new HTMLTextPane();
      textArea.setText(text);
      textArea.setMaximumWidth(200);
      textArea.setLocation(PADDING_X,PADDING_Y);

      Rectangle rect = textArea.getBounds();
      rect.grow(PADDING_X,PADDING_Y);      
      rbox = new RoundedBox(sikuliGuide,rect);
      
      add(textArea);
      add(rbox);

      triangle = new Triangle(sikuliGuide);  
      triangle.setForeground(Color.yellow);
      triangle.setLocationRelativeTo(rbox, SikuliGuideComponent.BOTTOM);
      add(triangle);

   }

   int dx=0;
   int dy=0;
   public void setLocationRelativeToRegion(Region region, int side) {

      if (side == TOP){

         triangle.setLocationRelativeTo(rbox, SikuliGuideComponent.BOTTOM);

      } else if (side == BOTTOM){

         dy = TRIANGLE_SIZE;

         triangle.rotate(Math.PI);
         triangle.setLocationRelativeTo(rbox, SikuliGuideComponent.TOP);

      } else if (side == LEFT){

         triangle.rotate(-Math.PI/2);
         triangle.setLocationRelativeTo(rbox, SikuliGuideComponent.RIGHT);

      } else if (side == RIGHT){

         dx = TRIANGLE_SIZE;

         triangle.rotate(Math.PI/2);
         triangle.setLocationRelativeTo(rbox, SikuliGuideComponent.LEFT);
      }      

      Rectangle bounds = rbox.getBounds();
      bounds.add(triangle.getBounds());
      setBounds(bounds);

      super.setLocationRelativeToRegion(region,side);
   }


   public void paintComponent(Graphics g){
      g.translate(dx, dy);
      super.paintComponent(g);
   }      

}
