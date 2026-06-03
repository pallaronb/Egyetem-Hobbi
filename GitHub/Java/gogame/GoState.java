package gogame;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static gogame.BoardSpace.fromStone;
import static java.util.Arrays.stream;

public class GoState implements Predicate<Point>, Serializable {

    public BoardSpace[][] board;
    private int blackCaptured;
    private int whiteCaptured;
    public Stone turn;

    private Set<GoState> koHistory;

    public GoState(int size){
        this.blackCaptured = 0;
        this.whiteCaptured = 0;
        this.turn = Stone.BLACK;
        koHistory = new HashSet<>();
        this.board = IntStream.range(0, size)
                .mapToObj(i -> IntStream.range(0, size)
                        .mapToObj(j -> BoardSpace.EMPTY)
                        .toArray(BoardSpace[]::new))
                .toArray(BoardSpace[][]::new);
    }

    public GoState(GoState other){
        this.blackCaptured = other.blackCaptured;
        this.whiteCaptured = other.whiteCaptured;
        this.turn = other.turn;
        this.board = stream(other.board)
                .map( row -> stream(row).toArray(BoardSpace[]::new))
                .toArray(BoardSpace[][]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GoState goState = (GoState) o;
        return Objects.deepEquals(board, goState.board) && turn == goState.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(board), turn);
    }

    @Override
    public boolean test(Point p) {
        if(p == null){
            return false;
        }
        int limit = board.length;

        return p.getX() >= 0 && p.getX() < limit &&
                p.getY() >= 0 && p.getY() < limit;
    }

    public int getWhiteCaptured() {
        return this.whiteCaptured;
    }

    public Point[] getNeighbors(Point p){
        Point[] pointsToCheck;
        pointsToCheck = new Point[]{new Point(p.getX(), p.getY() - 1),
                new Point(p.getX(), p.getY() + 1),
                new Point(p.getX() + 1, p.getY()),
                new Point(p.getX() - 1, p.getY())
        };
        return stream(pointsToCheck)
                .filter((point) -> test(point))
                .toArray(Point[]::new);
    }

    public Point[] getLiberties(Stone s, Point p, Set<Point> scanned) {
        Point[] liberties = new Point[board.length * board.length];
        Queue<Point> toScan = new LinkedList<>();
        Set<Point> seen = new HashSet<>();
        toScan.add(p);
        seen.add(p);
        int i = 0;
        while (!toScan.isEmpty()) {
            Point current = toScan.poll();

            BoardSpace space = board[current.getX()][current.getY()];
            if (space == BoardSpace.EMPTY) {
                liberties[i] = current;
            }
            else if (space == fromStone(s)) {
                scanned.add(current);
            }
            if (space == fromStone(s) || current.equals(p)) {
                for (Point neighbor : getNeighbors(current)) {
                    if (!seen.contains(neighbor)) {
                        seen.add(neighbor);
                        toScan.add(neighbor);
                    }
                }
            }
            i++;
        }
        return liberties;
    }

    public void checkCaptured(Point p){
        Set<Point> scanned = new HashSet<>();
        Point[] liberties = getLiberties(turn, p, scanned);
        if(liberties.length == 0){
            scanned.forEach((point) -> {
                        board[point.getX()][point.getY()] = BoardSpace.EMPTY;
                        if (turn == Stone.BLACK) {
                            whiteCaptured++;
                        } else {
                            blackCaptured++;
                        }
                    }
            );
        }
    }

    public GoState placeStone(Point p){
        board[p.getX()][p.getY()] = fromStone(turn);
        Point[] neighbours = getNeighbors(p);
        stream(neighbours)
                .forEach((point) -> checkCaptured(point));
        return this;
    }

    public boolean isLegalMove(Point p) {
        if (!test(p) || board[p.getX()][p.getY()] != BoardSpace.EMPTY) {
            return false;
        }
        boolean hasEmptyNeighbor = Arrays.stream(getNeighbors(p))
                .anyMatch(n -> board[n.getX()][n.getY()] == BoardSpace.EMPTY);
        boolean friendlyHasLiberties = Arrays.stream(getNeighbors(p))
                .filter(n -> board[n.getX()][n.getY()] == fromStone(turn))
                .anyMatch(n -> getLiberties(turn, n, new HashSet<>()).length > 1);
        boolean capturesEnemy = Arrays.stream(getNeighbors(p))
                .filter(n -> board[n.getX()][n.getY()] == fromStone(turn.opposite()))
                .map(n -> getLiberties(turn.opposite(), n, new HashSet<>()).length)
                .anyMatch(liberties -> liberties == 1);
        if (!hasEmptyNeighbor && !friendlyHasLiberties && !capturesEnemy) {
            return false;
        }
        GoState nextState = new GoState(this).placeStone(p);
        if (koHistory.contains(nextState)) {
            return false;
        }
        return true;
    }

    public boolean makeMove(Point p) {
        if (p == null) {
            koHistory.add(new GoState(this));
            this.turn = this.turn.opposite();
            return true;
        } else {
            if (!isLegalMove(p)) {
                return false;
            }
            koHistory.add(new GoState(this));
            placeStone(p);
            this.turn = this.turn.opposite();
            return true;
        }
    }

    @Override
    public String toString(){
        return "Black Captured: " + blackCaptured + "\nWhite Captured: " + whiteCaptured;
    }

    public static GoState loadGame(File filename){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){
            return (GoState) in.readObject();
        } catch (Exception e){
            throw new IllegalArgumentException("Failed to load game from: " + filename);
        }
    }

    public void saveGame(File filename){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
            out.writeObject(this);
        }catch(Exception e){
            throw new IllegalArgumentException("Failed to save game to: " + filename);
        }
    }

}
