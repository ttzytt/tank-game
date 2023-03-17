package utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import externals.Scalr;

public class ImagePanel extends JPanel {
    private BufferedImage dspImg;
    private BufferedImage origImg;
    private int owid, ohei;
    private double aspRate;
    private String dbgL;
    private Dimension lstDspSiz = new Dimension(0, 0);
    public ImagePanel(String path) {
        setVisible(true);
        setOpaque(false);
        try {
            dspImg = ImageIO.read(new File(path));
            origImg = ImageIO.read(new File(path));
            owid = origImg.getWidth();
            ohei = origImg.getHeight();
            aspRate = (double) owid / (double) ohei;
        } catch (IOException ex) {
            System.out.println("exception thrown when opening image file, path = " + path + " " + ex.getMessage());
            // handle exception...
        }
    }

    public ImagePanel(ImagePanel ip) {
        setVisible(true);
        BufferedImage img = ip.getBufImg();
        setOpaque(false);
        dspImg = img;
        origImg = img;
        owid = origImg.getWidth();
        ohei = origImg.getHeight();
        aspRate = (double) owid / (double) ohei;
    }

    public void updImg(BufferedImage img){
        dspImg = img;
        origImg = img;
        owid = origImg.getWidth();
        ohei = origImg.getHeight();
        aspRate = (double) owid / (double) ohei;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void deepCopy(ImagePanel ip) {
        BufferedImage img = ip.getBufImg();
        setOpaque(false);
        dspImg = deepCopy(img);
        origImg = deepCopy(img);
        owid = origImg.getWidth();
        ohei = origImg.getHeight();
        aspRate = (double) owid / (double) ohei;
    }

    public ImagePanel deepCopy(){
        ImagePanel ip = new ImagePanel(this);
        ip.dspImg = deepCopy(dspImg);
        ip.origImg = deepCopy(origImg);
        return ip;
    }


    public void setDbgL(String s){
        dbgL = s;
    }

    /**
     * resize the image displayzed in the image panel
     * 
     * @param wid the width of the image
     * @param hei the height of the image
     * @author tzyt
     */
    public void resize(int wid, int hei) {
        // Image tmp = origImg.getScaledInstance(wid, hei, Image.SCALE_SMOOTH);
        // dspImg = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
        // dspImg.getGraphics().drawImage(tmp, 0, 0, null);
        dspImg = Scalr.resize(origImg, Scalr.Method.SPEED, wid, hei);
    }

    public void resize(Coord siz){
        resize((int)siz.x, (int)siz.y);
    }

    public void resize(Dimension d) {
        resize(d.width, d.height);
    }

    public ImagePanel(String path, int wid, int hei) {
        try {
            dspImg = ImageIO.read(new File(path));
            origImg = ImageIO.read(new File(path));
            owid = origImg.getWidth();
            ohei = origImg.getHeight();
            aspRate = (double) owid / (double) ohei;
        } catch (IOException ex) {
            // handle exception...
        }
        resize(wid, hei);
    }

    public BufferedImage getDspImg() {
        return dspImg;
    }

    public void setDspImg(BufferedImage dspImg) {
        this.dspImg = dspImg;
    }

    public BufferedImage getOrigImg() {
        return origImg;
    }

    public void setOrigImg(BufferedImage origImg) {
        this.origImg = origImg;
    }

    public int getOwid() {
        return owid;
    }

    public void setOwid(int owid) {
        this.owid = owid;
    }

    public int getOhei() {
        return ohei;
    }

    public void setOhei(int ohei) {
        this.ohei = ohei;
    }

    public double getAspRate() {
        return aspRate;
    }

    public void setAspRate(double aspRate) {
        this.aspRate = aspRate;
    }

    public BufferedImage getBufImg() {
        return dspImg;
    }

    public void setBufImg(BufferedImage bufImg) {
        this.dspImg = bufImg;
    }

    /**
     * try to maximize the image into the given space, but keep the aspect ratio
     * if one given dimension is larger than the image, center the image in this
     * dimension
     * 
     * @author tzyt
     */
    @Override
    protected void paintComponent(Graphics g) {
        //// System.out.println("repaint called");
        super.paintComponent(g);
        Dimension dependW = new Dimension(getWidth(), (int) Math.round((double) getWidth() / aspRate));
        Dimension dependH = new Dimension((int) Math.round((double) getHeight() * aspRate), getHeight());

        if (dependW.height <= getHeight()) {
            // resize the image but make sure that the asp ratio is correct
            resize(dependW);
        } else {
            resize(dependH);
        }

        // default is to center the image
        int wOff = getWidth() - dspImg.getWidth();
        int hOff = getHeight() - dspImg.getHeight();

        // draw dbgL
        // System.out.println(dbgL);
        g.drawImage(dspImg, (int) Math.round((double) wOff / 2.0), (int) Math.round((double) hOff / 2.0), this);
        if(dbgL != null){
            g.setColor(Color.orange);
            g.drawString(dbgL,1, 10);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        //// System.out.println("getPreferredSize called");
        return new Dimension(owid, ohei);
    }

    @Override 
    public void setBounds(Rectangle r){
        super.setBounds(r);
        lstDspSiz = new Dimension(r.width, r.height);
    }
    @Override
    public int getWidth(){
        return lstDspSiz.width;
    }
    @Override
    public int getHeight(){
        return lstDspSiz.height;
    }

}
