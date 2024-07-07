import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MyFrame extends JFrame implements MouseListener,Runnable {

    Tile[][] tileMatrix = new Tile[8][8];
    private Thread gameThread;
    boolean setBlack = true;


    Tile selectedTile = null; // Track the selected tile
    JLabel selectedPiece = null; // Track the selected piece

    boolean whiteTurn = true;

    MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // makes x button exit application
        this.setLayout(null); // makes it so no layout is followed
        this.setVisible(true); // makes frame visible
        this.setSize(815, 838); // window dimensions
        this.setTitle("chess"); // title of window
        this.setResizable(false); // doesn't allow resizing the window
        this.getContentPane().setBackground(Color.red); // changes background color

        ImageIcon image = new ImageIcon("src/icon.awdasd"); // create ann ImageIcon
        this.setIconImage(image.getImage()); // sets the icon image to the ImageIcon object

        int currentY = -100;
        for(int i=0; i<8; i++){
            setBlack = !setBlack;
            currentY += 100;
            int currentX = -100;
            for(int j=0; j<8; j++) {
                currentX += 100;
                Tile aTile = new Tile(currentX, currentY, i, j);
                aTile.setTileColor(setBlack);
                setBlack = !setBlack;

                aTile.addMouseListener(this);
                this.add(aTile);
                tileMatrix[i][j] = aTile; // Store the panel in the array
            }
        }

        this.setUpBoard();

    }


    public void setUpBoard(){
            setUpPawns();
            setUpRooks();
            setUpKnights();
            setUpBishop();
            setUpKing();
            setUpQueen();
    }

    public void setUpPawns(){
        for (int i=1; i<=6; i+=5){
            for (int j=0; j<8; j++){
                ImageIcon aPiece;
                if (i==1){
                    aPiece = new ImageIcon("src/images/blackPawn.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isBlackPiece = true;
                } else{
                    aPiece = new ImageIcon("src/images/whitePawn.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isWhitePiece = true;
                }
                tileMatrix[i][j].hasPawn = true;
            }
        }
    }
    public void setUpRooks(){
        for (int i=0; i<8; i+=7){
            for (int j=0; j<8; j+=7){
                ImageIcon aPiece;
                if (i==0){
                    aPiece = new ImageIcon("src/images/blackRook.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isBlackPiece = true;
                } else{
                    aPiece = new ImageIcon("src/images/whiteRook.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isWhitePiece = true;
                }
                tileMatrix[i][j].hasRook = true;
            }
        }
    }
    public void setUpKnights(){
        for (int i=0; i<8; i+=7){
            for (int j=1; j<7; j+=5){
                ImageIcon aPiece;
                if (i==0){
                    aPiece = new ImageIcon("src/images/blackHorse.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isBlackPiece = true;
                } else{
                    aPiece = new ImageIcon("src/images/whiteHorse.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isWhitePiece = true;
                }
                tileMatrix[i][j].hasKnight = true;
            }
        }
    }
    public void setUpBishop(){
        for (int i=0; i<8; i+=7){
            for (int j=2; j<6; j+=3){
                ImageIcon aPiece;
                if (i==0){
                    aPiece = new ImageIcon("src/images/blackBishop.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isBlackPiece = true;
                } else{
                    aPiece = new ImageIcon("src/images/whiteBishop.png");
                    JLabel pieceLabel = new JLabel(aPiece);
                    tileMatrix[i][j].add(pieceLabel);
                    tileMatrix[i][j].isWhitePiece = true;
                }
                tileMatrix[i][j].hasBishop = true;
            }
        }
    }
    public void setUpKing(){
        for (int i=0; i<8; i+=7){
            ImageIcon aPiece;
            if (i==0){
                aPiece = new ImageIcon("src/images/blackKing.png");
                JLabel pieceLabel = new JLabel(aPiece);
                tileMatrix[i][4].add(pieceLabel);
                tileMatrix[i][4].isBlackPiece = true;
            } else{
                aPiece = new ImageIcon("src/images/whiteKing.png");
                JLabel pieceLabel = new JLabel(aPiece);
                tileMatrix[i][4].add(pieceLabel);
                tileMatrix[i][4].isWhitePiece = true;
            }
            tileMatrix[i][4].hasKing = true;
        }
    }
    public void setUpQueen(){
        for (int i=0; i<8; i+=7){
            ImageIcon aPiece;
            if (i==0){
                aPiece = new ImageIcon("src/images/blackQueen.png");
                JLabel pieceLabel = new JLabel(aPiece);
                tileMatrix[i][3].add(pieceLabel);
                tileMatrix[i][3].isBlackPiece = true;
            } else{
                aPiece = new ImageIcon("src/images/whiteQueen.png");
                JLabel pieceLabel = new JLabel(aPiece);
                tileMatrix[i][3].add(pieceLabel);
                tileMatrix[i][3].isWhitePiece = true;
            }
            tileMatrix[i][3].hasQueen = true;
        }
    }

    private boolean isCorrectTurn(Tile source) {
        return (source.isWhitePiece && whiteTurn) || (source.isBlackPiece && !whiteTurn);
    }

    public void piecePath(Tile source) {
        if (source.hasPawn && isCorrectTurn(source)) {
            pawnPath(source);
        }
        if ((source.hasRook || source.hasQueen) && isCorrectTurn(source)) {
            rookPath(source);
        }
        if (source.hasKnight && isCorrectTurn(source)) {
            knightPath(source);
        }
        if ((source.hasBishop || source.hasQueen) && isCorrectTurn(source)) {
            bishopPath(source);
        }
        if (source.hasKing && isCorrectTurn(source)) {
            kingPath(source);
        }
    }

    public void pawnPath(Tile source)  {
        int r = source.tileRow;
        int c = source.tileColumn;
        source.mark();

        if (source.isWhitePiece) {
            if (r > 0 && tileMatrix[r-1][c].isEmpty()) {
                tileMatrix[r-1][c].mark();
                if (source.tileRow == 6 && r-2 >= 0 && tileMatrix[r-2][c].isEmpty()) {
                    tileMatrix[r-2][c].mark();
                }
            }

            if (r > 0 && c-1 >= 0 && tileMatrix[r-1][c-1].isOpposingPiece(source)) {
                tileMatrix[r-1][c-1].mark();
            }

            if (r > 0 && c+1 < tileMatrix[0].length && tileMatrix[r-1][c+1].isOpposingPiece(source)) {
                tileMatrix[r-1][c+1].mark();
            }
        }

        if (source.isBlackPiece) {
            if (r < tileMatrix.length - 1 && tileMatrix[r+1][c].isEmpty()) {
                tileMatrix[r+1][c].mark();
                if (source.tileRow == 1 && r+2 < tileMatrix.length && tileMatrix[r+2][c].isEmpty()) {
                    tileMatrix[r+2][c].mark();
                }
            }

            if (r < tileMatrix.length - 1 && c-1 >= 0 && tileMatrix[r+1][c-1].isOpposingPiece(source)) {
                tileMatrix[r+1][c-1].mark();
            }

            if (r < tileMatrix.length - 1 && c+1 < tileMatrix[0].length && tileMatrix[r+1][c+1].isOpposingPiece(source)) {
                tileMatrix[r+1][c+1].mark();
            }
        }
    }
    public boolean pawnWin(Tile pawn) {
        int r = pawn.tileRow;
        int c = pawn.tileColumn;
        boolean markKing = false;

        if (pawn.isWhitePiece) {
            if (r > 0) {
                if (c > 0 && tileMatrix[r-1][c-1].isOpposingPiece(pawn) && tileMatrix[r-1][c-1].hasKing) {
                    markKing = true;
                }
                if (c < tileMatrix[0].length - 1 && tileMatrix[r-1][c+1].isOpposingPiece(pawn) && tileMatrix[r-1][c+1].hasKing) {
                    markKing = true;
                }
            }
        }

        if (pawn.isBlackPiece) {
            if (r < tileMatrix.length - 1) {
                if (c > 0 && tileMatrix[r+1][c-1].isOpposingPiece(pawn) && tileMatrix[r+1][c-1].hasKing) {
                    markKing = true;
                }
                if (c < tileMatrix[0].length - 1 && tileMatrix[r+1][c+1].isOpposingPiece(pawn) && tileMatrix[r+1][c+1].hasKing) {
                    markKing = true;
                }
            }
        }

        return markKing;
    }

    public void rookPath(Tile source){
        int initialRow = source.tileRow;
        int initialColumn = source.tileColumn;
        source.mark();

        int r = initialRow;
        int c = initialColumn;
        while (r > 0 && (tileMatrix[r - 1][c].isEmpty() || tileMatrix[r - 1][c].isOpposingPiece(source))) {
            r -= 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }

        r = initialRow;
        while (r < tileMatrix.length - 1 && (tileMatrix[r + 1][c].isEmpty() || tileMatrix[r + 1][c].isOpposingPiece(source))) {
            r += 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }

        r = initialRow;
        while (c > 0 && (tileMatrix[r][c - 1].isEmpty() || tileMatrix[r][c - 1].isOpposingPiece(source)) ){
            c -= 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }

        c = initialColumn;
        while (c < tileMatrix[0].length - 1 && (tileMatrix[r][c + 1].isEmpty() || tileMatrix[r][c + 1].isOpposingPiece(source)) ) {
            c += 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }
    }
    public boolean rookWin(Tile source){
        int initialRow = source.tileRow;
        int initialColumn = source.tileColumn;
        boolean markKing = false;

        int r = initialRow;
        int c = initialColumn;
        while (r > 0 && (tileMatrix[r - 1][c].isEmpty() || tileMatrix[r - 1][c].isOpposingPiece(source))) {
            r -= 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }

        r = initialRow;
        while (r < tileMatrix.length - 1 && (tileMatrix[r + 1][c].isEmpty() || tileMatrix[r + 1][c].isOpposingPiece(source))) {
            r += 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }

        r = initialRow;
        while (c > 0 && (tileMatrix[r][c - 1].isEmpty() || tileMatrix[r][c - 1].isOpposingPiece(source)) ){
            c -= 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }

        c = initialColumn;
        while (c < tileMatrix[0].length - 1 && (tileMatrix[r][c + 1].isEmpty() || tileMatrix[r][c + 1].isOpposingPiece(source))) {
            c += 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)){
                break;
            }
        }
        return markKing;
    }

    public void knightPath(Tile source){
        int r = source.tileRow;
        int c = source.tileColumn;
        source.mark();


        int[][] moves = {
                {r + 2, c + 1}, {r + 2, c - 1},
                {r - 2, c + 1}, {r - 2, c - 1},
                {r + 1, c + 2}, {r + 1, c - 2},
                {r - 1, c + 2}, {r - 1, c - 2}
        };

        for (int[] move : moves) {
            int newR = move[0];
            int newC = move[1];

            if (newR >= 0
                    && newR < tileMatrix.length
                    && newC >= 0
                    && newC < tileMatrix[0].length
                    && (tileMatrix[newR][newC].isEmpty() ||tileMatrix[newR][newC].isOpposingPiece(source) )) {
                tileMatrix[newR][newC].mark();
            }
        }

    }
    public boolean knightWin(Tile source){
        int r = source.tileRow;
        int c = source.tileColumn;
        boolean markKing = false;


        int[][] moves = {
                {r + 2, c + 1}, {r + 2, c - 1},
                {r - 2, c + 1}, {r - 2, c - 1},
                {r + 1, c + 2}, {r + 1, c - 2},
                {r - 1, c + 2}, {r - 1, c - 2}
        };

        for (int[] move : moves) {
            int newR = move[0];
            int newC = move[1];

            if (newR >= 0
                    && newR < tileMatrix.length
                    && newC >= 0
                    && newC < tileMatrix[0].length
                    && (tileMatrix[newR][newC].isEmpty() ||tileMatrix[newR][newC].isOpposingPiece(source) )) {
                if (tileMatrix[newR][newC].hasKing){
                    markKing = true;
                }
            }
        }
        return markKing;
    }

    public void bishopPath(Tile source) {
        int initialRow = source.tileRow;
        int initialColumn = source.tileColumn;
        source.mark();

        int r = initialRow;
        int c = initialColumn;

        while (r > 0 && c < 7 && (tileMatrix[r - 1][c + 1].isEmpty() || tileMatrix[r - 1][c + 1].isOpposingPiece(source))) {
            r -= 1;
            c += 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }

        r = initialRow;
        c = initialColumn;

        while (r > 0 && c > 0 && (tileMatrix[r - 1][c - 1].isEmpty() || tileMatrix[r - 1][c - 1].isOpposingPiece(source))) {
            r -= 1;
            c -= 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }

        r = initialRow;
        c = initialColumn;

        while (r < 7 && c < 7 && (tileMatrix[r + 1][c + 1].isEmpty() || tileMatrix[r + 1][c + 1].isOpposingPiece(source))) {
            r += 1;
            c += 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }

        r = initialRow;
        c = initialColumn;

        while (r < 7 && c > 0 && (tileMatrix[r + 1][c - 1].isEmpty() || tileMatrix[r + 1][c - 1].isOpposingPiece(source))) {
            r += 1;
            c -= 1;
            tileMatrix[r][c].mark();
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }
    }
    public boolean bishopWin(Tile source){
        int initialRow = source.tileRow;
        int initialColumn = source.tileColumn;
        boolean markKing = false;

        int r = initialRow;
        int c = initialColumn;

        while (r > 0 && c < 7 && (tileMatrix[r - 1][c + 1].isEmpty() || tileMatrix[r - 1][c + 1].isOpposingPiece(source))) {
            r -= 1;
            c += 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }

        r = initialRow;
        c = initialColumn;

        while (r > 0 && c > 0 && (tileMatrix[r - 1][c - 1].isEmpty() || tileMatrix[r - 1][c - 1].isOpposingPiece(source))) {
            r -= 1;
            c -= 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }

        r = initialRow;
        c = initialColumn;

        while (r < 7 && c < 7 && (tileMatrix[r + 1][c + 1].isEmpty() || tileMatrix[r + 1][c + 1].isOpposingPiece(source))) {
            r += 1;
            c += 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }

        r = initialRow;
        c = initialColumn;

        while (r < 7 && c > 0 && (tileMatrix[r + 1][c - 1].isEmpty() || tileMatrix[r + 1][c - 1].isOpposingPiece(source))) {
            r += 1;
            c -= 1;
            if (tileMatrix[r][c].hasKing){
                markKing = true;
            }
            if (tileMatrix[r][c].isOpposingPiece(source)) {
                break;
            }
        }
        return markKing;
    }

    public void kingPath(Tile source){
        int r = source.tileRow;
        int c = source.tileColumn;
        source.mark();

        int[][] moves = {
                {r + 1, c}, {r - 1, c},
                {r, c + 1}, {r, c - 1},
                {r + 1, c + 1}, {r + 1, c - 1},
                {r - 1, c + 1}, {r - 1, c - 1}
        };

        for (int[] move : moves) {
            int newR = move[0];
            int newC = move[1];

            if (newR >= 0
                    && newR < tileMatrix.length
                    && newC >= 0 && newC < tileMatrix[0].length
                    && (tileMatrix[newR][newC].isEmpty() || tileMatrix[newR][newC].isOpposingPiece(source))) {
                tileMatrix[newR][newC].mark();
            }
        }

    }
    public boolean kingWin(Tile source){
        int r = source.tileRow;
        int c = source.tileColumn;
        boolean markKing = false;

        int[][] moves = {
                {r + 1, c}, {r - 1, c},
                {r, c + 1}, {r, c - 1},
                {r + 1, c + 1}, {r + 1, c - 1},
                {r - 1, c + 1}, {r - 1, c - 1}
        };

        for (int[] move : moves) {
            int newR = move[0];
            int newC = move[1];

            if (newR >= 0
                    && newR < tileMatrix.length
                    && newC >= 0 && newC < tileMatrix[0].length
                    && (tileMatrix[newR][newC].isEmpty() || tileMatrix[newR][newC].isOpposingPiece(source))) {
                if (tileMatrix[newR][newC].hasKing){
                    markKing = true;
                }
            }
        }
        return markKing;
    }

    public void changeTurn(){
        whiteTurn = !whiteTurn;
    }

    public void escapesCheck(Tile moveTo) {
        // Save current state
        Tile originalTile = selectedTile;
        JLabel originalPiece = selectedPiece;
        Component pieceAtDestination = null;

        if (!moveTo.isEmpty()) {
            pieceAtDestination = moveTo.getComponent(0);
        }

        // Simulate the move
        selectedTile.remove(selectedPiece);
        selectedTile.revalidate();

        if (pieceAtDestination != null) {
            moveTo.remove(pieceAtDestination);
        }
        moveTo.add(selectedPiece);
        moveTo.revalidate();

        updateBoardState(selectedTile,moveTo);

        // Check if the king is still in check
        boolean kingStillInCheck = isKingInCheck();

        if (kingStillInCheck){
            // Restore the original state
            moveTo.remove(selectedPiece);
            if (pieceAtDestination != null) {
                moveTo.add(pieceAtDestination);
            }
            moveTo.revalidate();

            originalTile.add(originalPiece);
            originalTile.revalidate();
            updateBoardState(moveTo,selectedTile);
        }

        selectedTile = null;
        selectedPiece = null;
        changeTurn();
        clean();

    }


    public void clean(){
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (tileMatrix[i][j].getBackground().equals(new Color(190, 204, 57))){
                    tileMatrix[i][j].setBackground(new Color(118,149,86));
                } else if (tileMatrix[i][j].getBackground().equals(new Color(246, 246, 104))){
                    tileMatrix[i][j].setBackground(new Color(238,238,211));
                }
            }
        }
    }


    public void mouseClicked(MouseEvent e) {
        Tile clickedTile = (Tile) e.getSource();

        if (selectedTile == null && !clickedTile.isEmpty()) {
            // Select the piece
            selectedTile = clickedTile;
            selectedPiece = (JLabel) clickedTile.getComponent(0);
            clean();
            piecePath(clickedTile);
        } else if ((clickedTile.isMarked() && clickedTile != selectedTile) && !isKingInCheck()) {
            movePiece(clickedTile);
        }else if ((clickedTile.isMarked() && clickedTile != selectedTile) && isKingInCheck()) {
            escapesCheck(clickedTile);
        } else {
            // Reset selection
            selectedTile = null;
            selectedPiece = null;
            clean();
        }
    }

    private void movePiece(Tile destination) {
        if (destination != null) {
            if (destination.isOpposingPiece(selectedTile) && destination.isMarked()) {
                destination.removeAll();
            }

            selectedTile.remove(selectedPiece);
            selectedTile.revalidate();
            selectedTile.repaint();

            destination.add(selectedPiece);
            destination.revalidate();
            destination.repaint();

            updateBoardState(selectedTile, destination);

            selectedTile = null;
            selectedPiece = null;
            changeTurn();
            clean();
        }
    }

    private void updateBoardState(Tile source, Tile destination) {
        // Copy piece state from source to destination
        destination.isBlackPiece = source.isBlackPiece;
        destination.isWhitePiece = source.isWhitePiece;
        destination.hasKing = source.hasKing;
        destination.hasQueen = source.hasQueen;
        destination.hasRook = source.hasRook;
        destination.hasBishop = source.hasBishop;
        destination.hasKnight = source.hasKnight;
        destination.hasPawn = source.hasPawn;

        // Clear the source tile state
        source.isBlackPiece = false;
        source.isWhitePiece = false;
        source.hasKing = false;
        source.hasQueen = false;
        source.hasRook = false;
        source.hasBishop = false;
        source.hasKnight = false;
        source.hasPawn = false;
    }

    public void checkWin(){
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;

        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (tileMatrix[i][j].hasKing && tileMatrix[i][j].isBlackPiece){
                    blackKingAlive = true;
                }
                if (tileMatrix[i][j].hasKing && tileMatrix[i][j].isWhitePiece){
                    whiteKingAlive = true;
                }
            }
        }

        if (!whiteKingAlive){
            JOptionPane.showMessageDialog(this, "Black wins!");
        }
        if (!blackKingAlive){
            JOptionPane.showMessageDialog(this, "White wins!");
        }
    }

    public boolean isKingInCheck() {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile currentTile = tileMatrix[row][col];
                    // Check if this opponent's piece can attack the king
                if (currentTile.hasPawn){
                    if (canPawnAttack(row,col)) {
                        return true;
                    }
                } else if(currentTile.hasRook) {
                    if (canRookAttack(row,col)) {
                        return true;
                    }
                } else if (currentTile.hasKnight) {
                    if (canKnightAttack(row, col)) {
                        return true;
                    }
                } else if (currentTile.hasBishop) {
                    if (canBishopAttack(row, col)) {
                        return true;
                    }
                } else if (currentTile.hasQueen) {
                    if (canBishopAttack(row,col) || canRookAttack(row,col)){
                        return true;
                    }
                } else if (currentTile.hasKing) {
                    if (canKingAttack(row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canPawnAttack(int row, int col){
        return pawnWin(tileMatrix[row][col]);
    }
    public boolean canRookAttack(int row, int col){
        return rookWin(tileMatrix[row][col]);
    }
    public boolean canKnightAttack(int row, int col){
        return knightWin(tileMatrix[row][col]);
    }
    public boolean canBishopAttack(int row, int col){
        return bishopWin(tileMatrix[row][col]);
    }
    public boolean canKingAttack(int row, int col){
        return kingWin(tileMatrix[row][col]);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(gameThread != null){
            update();
            try {
                Thread.sleep(500); // Add a delay of 500 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(){
        if (isKingInCheck()){
            System.out.println("king is in check");
        }
        checkWin();
    }
}
