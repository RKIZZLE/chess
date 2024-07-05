import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {

    //dimensions and index
    int tileWidth = 100;
    int tileHeight = 100;
    int tileRow;
    int tileColumn;

    boolean isBlackTile;
    boolean isBlackPiece;
    boolean isWhitePiece;

    //king, rook, bishop, queen, knight, and pawn
    boolean hasKing = false;
    boolean hasQueen = false;
    boolean hasRook = false;
    boolean hasBishop = false;
    boolean hasKnight = false;
    boolean hasPawn = false;

    Tile(int x, int y, int r, int c){
        this.setSize(tileWidth,tileHeight);
        this.setLocation(x,y);
        this.tileRow = r;
        this.tileColumn = c;
    }

    //decides whether tile is black tile or white tile
    public void setTileColor(boolean setBlack){
        if(setBlack){
            this.setBackground(new Color(118,149,86));
            isBlackTile=true;
        }else{
            this.setBackground(new Color(238,238,211));
            isBlackTile = false;
        }
    }

    public void mark(){
        if (isBlackTile){
            this.setBackground(new Color(190, 204, 57));
        } else {
            this.setBackground(new Color(246, 246, 104));
        }
    }

    public boolean isMarked() {
        return this.getBackground().equals(new Color(190, 204, 57)) || this.getBackground().equals(new Color(246, 246, 104));
    }

    public boolean isEmpty(){
        if(!hasKing && !hasQueen && !hasRook && !hasBishop && !hasKnight &&!hasPawn){
            return true;
        }
        return false;
    }

    public boolean isOpposingPiece(Tile source) {
        return (this.isBlackPiece && source.isWhitePiece) || (this.isWhitePiece && source.isBlackPiece);
    }

}
