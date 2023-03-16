package gameElements;
import java.awt.*;
// import gaphics 2d
import javax.swing.*;
import utils.Consts;
import utils.Coord;
import utils.ImagePanel;
import java.awt.image.BufferedImage;

public class DbgBox extends BoundingBox{
    Coord imgSize;
    Color imgCol;
    BufferedImage bufImg;
    public DbgBox(Coord pos, Coord siz, Color col, String txt, int id) {
        super(pos, Coord.zero());
        imgCol = col;
        this.id = id;
        imgSize = siz;
        img = new ImagePanel(Consts.TRANSPARENT_IMG);
    }
    @Override
    public void setImgBound(int pixPerBlk){
        if (img != null){
            Coord muledSiz = imgSize.mul(pixPerBlk);
            img.setBounds(Coord.toRect(pos.mul(pixPerBlk), muledSiz));
            bufImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bufImg.createGraphics();
            g.setColor(imgCol);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.dispose();
            img.updImg(bufImg);
            img.setBounds(Coord.toRect(pos.mul(pixPerBlk), muledSiz));
        }
    }
}  
